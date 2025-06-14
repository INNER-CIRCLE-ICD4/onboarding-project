package fastcampus.onboarding.form.service;

import fastcampus.onboarding.common.exception.CustomException;
import fastcampus.onboarding.common.exception.ErrorCode;
import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.entity.Form;
import fastcampus.onboarding.form.entity.Item;
import fastcampus.onboarding.form.entity.Option;
import fastcampus.onboarding.form.repository.FormRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
                    item.addOption(option); // 양방향 연관관계 설정
                });

                return item;
            }).collect(Collectors.toList());

            form.setItem(itemList); // 양방향 연관관계 설정
            formRepository.save(form); // cascade 덕분에 item, option 모두 저장

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
