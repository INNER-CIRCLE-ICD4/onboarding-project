package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerToDomainMapper {

    public static Answer toDomain(AnswerJpaEntity entity) {
        /*List<AnswerDetail> details = entity.getDetails() == null ? Collections.emptyList() :
                entity.getDetails().stream()
                        .map(detailDto -> AnswerDetail.builder()
                                .contentId(detailDto.getContentId())
                                .optionsId(detailDto.getOptionId())
                                .answerValue(detailDto.getAnswerValue())
                                .build()
                        ).collect(Collectors.toList());

        return Answer.builder()
                .id(entity.getId()) // id 필드가 있다면 추가
                .formId(entity.getFormId())
                .submittedAt(entity.getSubmittedAt()) // DB 값 사용
                .details(details)
                .build();*/
        Answer.builder()
                .formId(entity.getFormId())
                .submittedAt(entity.getSubmittedAt())
                .details()


        return new Answer();
    }
}
