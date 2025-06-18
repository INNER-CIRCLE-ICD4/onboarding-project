package fastcampus.onboarding.form.service;

import fastcampus.onboarding.common.exception.CustomException;
import fastcampus.onboarding.common.exception.ErrorCode;
import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.dto.request.FormUpdateRequestDto;
import fastcampus.onboarding.form.dto.request.ItemRequestDto;
import fastcampus.onboarding.form.dto.request.ItemUpdateRequestDto;
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
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;

    @Transactional
    public void createForm(FormCreateRequestDto dto) {
        try {
            // 1. 항목 개수 제한 검사
            if (dto.getItems().size() > 10) {
                throw new IllegalArgumentException("항목은 최대 10개까지만 등록할 수 있습니다.");
            }

            // 2. 양식 생성
            Form form = Form.builder()
                    .formTitle(dto.getFormTitle())
                    .formContent(dto.getFormContent())
                    .build();

            // 3. 선택지 문항 처리
            List<Item> itemList = IntStream.range(0, dto.getItems().size())
                    .mapToObj(i -> {
                        ItemRequestDto itemDto = dto.getItems().get(i);

                        Item item = Item.builder()
                                .itemTitle(itemDto.getItemTitle())
                                .itemContent(itemDto.getItemContent())
                                .itemType(itemDto.getItemType())
                                .isRequired(itemDto.getIsRequired())
                                .itemSeq(i + 1)
                                .build();

                        item.setForm(form);

                        itemDto.getOptions().forEach(optionDto -> {
                            Option option = Option.builder()
                                    .optionContent(optionDto.getOptionContent())
                                    .build();
                            item.addOption(option);
                        });

                        return item;
                    })
                    .collect(Collectors.toList());


            form.setItems(itemList);

            // 4. 저장
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
            Map<Integer, Item> existingItemMap = mapItemsById(form.getItems());

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

    private Map<Integer, Item> mapItemsById(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(Item::getItemSeq, item -> item));
    }

    private Item updateItem(ItemUpdateRequestDto itemDto, Map<Integer, Item> existingItemMap, Form form) {
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

    private void updateOptions(Item item, ItemUpdateRequestDto itemDto) {
        Map<Integer, Option> existingOptionMap = item.getOptions().stream()
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
