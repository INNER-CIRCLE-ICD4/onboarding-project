package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailOptionJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;

import java.util.List;
import java.util.stream.Collectors;

public class AnswerToEntityMapper {

    public static AnswerJpaEntity toJpaEntity(Answer answer) {
        AnswerJpaEntity entity = AnswerJpaEntity.builder()
                .formId(answer.getFormId())
                .submittedAt(answer.getSubmittedAt())
                .build();

        if (answer.getDetails() != null) {
            List<AnswerDetailJpaEntity> details = answer.getDetails().stream()
                    .map(detail -> toJpaEntity(detail, entity))
                    .collect(Collectors.toList());
            entity.setDetails(details);
        }
        return entity;
    }

    private static AnswerDetailJpaEntity toJpaEntity(AnswerDetail detail, AnswerJpaEntity answerEntity) {
        AnswerDetailJpaEntity entity = AnswerDetailJpaEntity.builder()
                .contentId(detail.getContentId())
                .contentName(detail.getContentName())
                .contentDescribe(detail.getContentDescribe())
                .type(detail.getType())
                .value(detail.getValue())
                .answer(answerEntity)
                .build();

        if (detail.getOptions() != null && !detail.getOptions().isEmpty()) {
            List<AnswerDetailOptionJpaEntity> options = detail.getOptions().stream()
                    .map(opt -> AnswerDetailOptionJpaEntity.builder()
                            .answerDetail(entity) // 부모 연결
                            .text(opt.getText()) // DTO에서 받아온 값
                            .build())
                    .collect(Collectors.toList());
            entity.setOptions(options);
        }
        return entity;
    }
}
