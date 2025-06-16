package fastcampus.onboarding.form.service;

import fastcampus.onboarding.common.exception.CustomException;
import fastcampus.onboarding.common.exception.ErrorCode;
import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.dto.request.FormUpdateRequestDto;
import fastcampus.onboarding.form.entity.Form;
import fastcampus.onboarding.form.entity.Item;
import fastcampus.onboarding.form.entity.Option;
import fastcampus.onboarding.form.repository.FormRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    @Transactional
    public void createForm(FormCreateRequestDto dto) {
        try {
            //1. 양식 생성
            Form form = Form.builder()
                    .formTitle(dto.getFormTitle())
                    .formContent(dto.getFormContent())
                    .build();

            //2. 선택지 문항 처리
            List<Item> itemList = dto.getItems().stream().map(itemDto -> {
                Item item = Item.builder()
                        .itemTitle(itemDto.getItemTitle())
                        .itemContent(itemDto.getItemContent())
                        .itemType(itemDto.getItemType())
                        .isRequired(itemDto.getIsRequired())
                        .build();

                item.setForm(form);

                //3. 선택지 문항 옵션 처리
                itemDto.getOptions().forEach(optionDto -> {
                    Option option = Option.builder()
                            .optionContent(optionDto.getOptionContent())
                            .build();
                    item.addOption(option);
                });

                return item;
            }).toList();

            formRepository.save(form);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public void updateForm(Long formSeq, FormUpdateRequestDto dto) {
        try {
            //양식 조회
            Form form = formRepository.findById(formSeq)
                    .orElseThrow(() -> new CustomException(ErrorCode.FORM_NOT_FOUND));

            //양식 수정사항 빌더
            form.updateForm(dto.getFormTitle(), dto.getFormContent());

            //존재하는 문항 처리
            Map<Long, Item> existingItemMap = mapItemsById(form.getItems());

            //수정문항 처리
            List<Item> updatedItems = dto.getItems().stream()
                    .map(itemDto -> updateItem(itemDto, existingItemMap, form))
                    .toList();

            //사라진 문항 삭제
            removeDeletedItems(form, updatedItems);
            //새로운 문항 추가
            addNewItemsToForm(form, updatedItems);

            formRepository.save(form);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<Long, Item> mapItemsById(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(Item::getItemSeq, item -> item));
    }

    private Item updateItem(FormUpdateRequestDto.ItemUpdateDto itemDto, Map<Long, Item> existingItemMap, Form form) {
        Item item;

        if (itemDto.getItemSeq() != null && existingItemMap.containsKey(itemDto.getItemSeq())) {
            item = existingItemMap.get(itemDto.getItemSeq());
            item.updateItem(itemDto.getItemTitle(), itemDto.getItemContent(), itemDto.getItemType(), itemDto.getIsRequired());
        } else {
            item = Item.builder()
                    .itemTitle(itemDto.getItemTitle())
                    .itemContent(itemDto.getItemContent())
                    .itemType(itemDto.getItemType())
                    .isRequired(itemDto.getIsRequired())
                    .build();
            form.addItem(item);
        }

        updateOptions(item, itemDto);
        return item;
    }

    private void updateOptions(Item item, FormUpdateRequestDto.ItemUpdateDto itemDto) {
        Map<Long, Option> existingOptionMap = item.getOptions().stream()
                .collect(Collectors.toMap(Option::getOptionSeq, option -> option));

        List<Option> updatedOptions = itemDto.getOptions().stream()
                .map(optionDto -> {
                    if (optionDto.getOptionSeq() != null && existingOptionMap.containsKey(optionDto.getOptionSeq())) {
                        Option option = existingOptionMap.get(optionDto.getOptionSeq());
                        option.setOptionContent(optionDto.getOptionContent());
                        return option;
                    } else {
                        Option newOption = Option.builder()
                                .optionContent(optionDto.getOptionContent())
                                .build();
                        item.addOption(newOption);
                        return newOption;
                    }
                }).toList();

        item.getOptions().removeIf(option -> !updatedOptions.contains(option));
    }

    private void removeDeletedItems(Form form, List<Item> updatedItems) {
        form.getItems().removeIf(item -> !updatedItems.contains(item));
    }

    private void addNewItemsToForm(Form form, List<Item> updatedItems) {
        for (Item item : updatedItems) {
            if (!form.getItems().contains(item)) {
                form.addItem(item);
            }
        }
    }
}
