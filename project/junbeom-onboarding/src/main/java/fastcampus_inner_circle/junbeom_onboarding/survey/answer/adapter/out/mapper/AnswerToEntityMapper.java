package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerToEntityMapper {

    // 도메인 -> JPA 엔티티
    public static AnswerJpaEntity toEntity(Answer domain) {
        AnswerJpaEntity entity = AnswerJpaEntity.builder()
                .formId(domain.getFormId())
                .submittedAt(domain.getSubmittedAt())
                .details(new ArrayList<>())
                .build();
        List<AnswerDetailJpaEntity> details = domain.getDetails().stream().map(detail ->
                AnswerDetailJpaEntity.builder()
                        .contentId(detail.getContentId())
                        .optionId(detail.getOptionId())
                        .answerValue(detail.getAnswerValue())
                        .answer(entity)
                        .build()
        ).collect(Collectors.toList());
        entity.getDetails().addAll(details);
        return entity;
    }


}
