package fastcampus.onboarding.form.service;

import fastcampus.onboarding.common.exception.CustomException;
import fastcampus.onboarding.common.exception.ErrorCode;
import fastcampus.onboarding.form.dto.request.AnswerRequestDto;
import fastcampus.onboarding.form.dto.response.*;
import fastcampus.onboarding.form.entity.*;
import fastcampus.onboarding.form.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerService {
    private final FormRepository formRepository;
    private final FormResponseRepository formResponseRepository;
    private final ItemRepsponseRepository itemRepsponseRepository;
    private final ItemRepository itemRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public void createdAnswer(Long formSeq, AnswerRequestDto answerRequestDto) {
        // 1. 설문지 조회: formSeq로 Form 엔티티 조회
        Form form = formRepository.findById(formSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.FORM_NOT_FOUND));

        log.info("form:::{}",form.toString());

        // 2. FormResponse 생성: 응답 세션 기록
        FormResponse formResponse = formResponseRepository.save(FormResponse.builder().form(form).build());

        // 3. Form의 모든 Item 조회: 필수 항목 검증 및 답변 처리용
        List<Item> formItems = itemRepository.findByForm(form);
        Map<Long, Item> itemMap = formItems.stream()
                .collect(Collectors.toMap(Item::getItemSeq, Function.identity()));

        log.info("itemMap:::{}",itemMap);

        // 4. 답변에서 itemSeq 추출
        List<Long> itemSeqs = answerRequestDto.getAnswers().stream()
                .map(AnswerRequestDto.ItemAnswerDto::getItemSeq)
                .collect(Collectors.toList());
        log.info("itemSeqs:::{}", Arrays.toString(itemSeqs.toArray()));

        // 5. 유효성 검증: 제출된 itemSeq가 Form에 속하는지 확인
        for (Long itemSeq : itemSeqs) {
            log.info("itemSeq::번쨰:{}", itemSeq);
            if (!itemMap.containsKey(itemSeq)) {
                throw new CustomException(ErrorCode.ITEM_NOT_FOUND, "항목(" + itemSeq + ")이 설문지에 속하지 않습니다.");
            }
        }

        // 6. 필수 항목 검증: 모든 필수 항목이 답변에 포함되었는지 확인
        for (Item item : formItems) {
            if (item.isRequired() && !itemSeqs.contains(item.getItemSeq())) {
                throw new CustomException(ErrorCode.REQUIRED_ITEM_MISSING, "필수 항목(" + item.getItemSeq() + ")이 누락되었습니다.");
            }
        }

        // 7. 답변 처리
        for (AnswerRequestDto.ItemAnswerDto answer : answerRequestDto.getAnswers()) {
            Item item = itemMap.get(answer.getItemSeq());

            // 8. ItemResponse 생성: 스냅샷 포함
            ItemResponse itemResponse = ItemResponse.builder()
                    .itemTitleSnapshot(item.getItemTitle())
                    .itemContentSnapshot(item.getItemContent())
                    .itemTypeSnapshot(item.getItemType().name())
                    .isRequiredSnapshot(item.isRequired())
                    .itemSeq(item.getItemSeq())
                    .build();
            itemResponse.setFormResponse(formResponse);

            // 9. ItemType별 처리
            ItemType itemType = item.getItemType();
            if (itemType == ItemType.SHORT_ANSWER || itemType == ItemType.PARAGRAPH) {
                // 단답형/장문형: textValue 저장
                if (item.isRequired() && (answer.getTextValue() == null || answer.getTextValue().isBlank())) {
                    throw new CustomException(ErrorCode.REQUIRED_ITEM_MISSING, "필수 항목(" + answer.getItemSeq() + ")의 답변이 비어 있습니다.");
                }
                itemResponse.setTextValue(answer.getTextValue());
            } else if (itemType == ItemType.SINGLE_CHOICE || itemType == ItemType.MULTIPLE_CHOICE) {
                // 단일/다중 선택: optionSeqs 처리
                List<Long> optionSeqs = answer.getOptionSeqs();
                if (item.isRequired() && (optionSeqs == null || optionSeqs.isEmpty())) {
                    throw new CustomException(ErrorCode.REQUIRED_ITEM_MISSING, "필수 항목(" + answer.getItemSeq() + ")의 선택지가 없습니다.");
                }
                if (optionSeqs != null && !optionSeqs.isEmpty()) {
                    List<Option> options = optionRepository.findByOptionSeqInAndItem(optionSeqs, item);
                    if (options.size() != optionSeqs.size()) {
                        throw new CustomException(ErrorCode.INVALID_OPTION, "유효하지 않은 선택지(" + optionSeqs + ")");
                    }
                    if (itemType == ItemType.SINGLE_CHOICE && optionSeqs.size() > 1) {
                        throw new CustomException(ErrorCode.INVALID_ANSWER, "단일 선택 항목에 다중 선택 포함");
                    }
                    // OptionSnapshot만 저장
                    List<OptionSnapshot> snapshots = options.stream()
                        .map(option -> {
                            OptionSnapshot snap = new OptionSnapshot();
                            snap.setOptionSeq(option.getOptionSeq());
                            snap.setOptionContentSnapshot(option.getOptionContent());
                            return snap;
                        })
                        .collect(Collectors.toList());
                    itemResponse.setSelectedOptionSnapshots(snapshots);
                }
            }

            // 10. ItemResponse 저장
            itemRepsponseRepository.save(itemResponse);
        }
    }

    @Transactional
    public AnswerDto getAnswer(Long formSeq) {
        // 1. 설문지 조회
        Form form = formRepository.findById(formSeq)
            .orElseThrow(() -> new CustomException(ErrorCode.FORM_NOT_FOUND));

        // 2. 응답 세션 조회
        List<FormResponse> formResponses = formResponseRepository.findByForm(form);

        // 응답 empth 검증
        if(formResponses.isEmpty()){
            throw new CustomException(ErrorCode.RESPONSE_NOT_FOUND);
        }

        // 모든 FormResponse를 AnswerGroupDto로 변환
        List<AnswerGroupDto> answerGroups = formResponses.stream()
                .map(formResponse -> {
                    // 검증
                    if (!formResponse.getForm().getFormSeq().equals(formSeq)) {
                        throw new CustomException(ErrorCode.NO_ANSWER);
                    }

                    List<ItemResponse> itemResponses = formResponse.getItemResponses();
                    if (itemResponses == null || itemResponses.isEmpty()) {
                        throw new CustomException(ErrorCode.NO_ITEM_RESPONSE);
                    }

                    List<AnswerItemDto> itemDtos = itemResponses.stream()
                            .map(itemResponse -> {
                                AnswerItemDto dto = AnswerItemDto.fromEntity(itemResponse);

                                ItemType itemType;
                                try {
                                    itemType = ItemType.valueOf(itemResponse.getItemTypeSnapshot());
                                } catch (IllegalArgumentException e) {
                                    throw new CustomException(ErrorCode.INVALID_ITEM_TYPE, "알 수 없는 문항 타입입니다: " + itemResponse.getItemTypeSnapshot());
                                }
                                dto.setItemType(itemType);

                                if (itemType == ItemType.SINGLE_CHOICE || itemType == ItemType.MULTIPLE_CHOICE) {
                                    List<OptionSnapshot> optionSnapshots = itemResponse.getSelectedOptionSnapshots();
                                    if (optionSnapshots == null || optionSnapshots.isEmpty()) {
                                        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "선택형 문항에 선택된 옵션이 없습니다.");
                                    }

                                    List<AnwerOptionDto> optionDtos = optionSnapshots.stream()
                                            .map(snap -> {
                                                log.info("snap:::{}", snap);
                                                AnwerOptionDto optDto = new AnwerOptionDto();
                                                optDto.setOptionSeq(snap.getOptionSeq());
                                                optDto.setOptionContent(snap.getOptionContentSnapshot());
                                                return optDto;
                                            })
                                            .collect(Collectors.toList());
                                    dto.setSelectedOptions(optionDtos);
                                }
                                log.info("dto:{}", dto.toString());
                                return dto;
                            })
                            .collect(Collectors.toList());

                    return new AnswerGroupDto(formResponse.getResponseSeq(), itemDtos);
                })
                .toList();

        // 5. 최종 Dto 조립
        AnswerDto answerDto = new AnswerDto();
        answerDto.setFormSeq(form.getFormSeq());
        answerDto.setFormTitle(form.getFormTitle());
        answerDto.setFormContent(form.getFormContent());
        answerDto.setAnswerItems(answerGroups);
        return answerDto;
    }
}
