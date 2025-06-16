package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerMapper {
    // DTO -> 도메인
    public static Answer toDomain(AnswerRequest dto) {
        List<AnswerDetail> details = dto.getAnswers().stream().map(detailDto ->
                AnswerDetail.builder()
                        .contentId(detailDto.getContentId())
                        .optionId(detailDto.getOptionId())
                        .answerValue(detailDto.getAnswerValue())
                        .build()
        ).collect(Collectors.toList());
        return Answer.builder()
                .formId(dto.getFormId())
                .submittedAt(LocalDateTime.now())
                .details(details)
                .build();
    }

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

    // JPA 엔티티 -> 도메인
    public static Answer toDomain(AnswerJpaEntity entity) {
        List<AnswerDetail> details = entity.getDetails().stream().map(detail ->
                AnswerDetail.builder()
                        .id(detail.getId())
                        .contentId(detail.getContentId())
                        .optionId(detail.getOptionId())
                        .answerValue(detail.getAnswerValue())
                        .build()
        ).collect(Collectors.toList());
        return Answer.builder()
                .id(entity.getId())
                .formId(entity.getFormId())
                .submittedAt(entity.getSubmittedAt())
                .details(details)
                .build();
    }

    // 도메인 -> DTO
    public static AnswerResponse toResponse(Answer domain) {
        AnswerResponse dto = new AnswerResponse();
        dto.setAnswerId(domain.getId());
        dto.setFormId(domain.getFormId());
        dto.setSubmittedAt(domain.getSubmittedAt());
        List<AnswerResponse.AnswerDetailDto> details = domain.getDetails().stream().map(detail -> {
            AnswerResponse.AnswerDetailDto d = new AnswerResponse.AnswerDetailDto();
            d.setContentId(detail.getContentId());
            d.setOptionId(detail.getOptionId());
            d.setAnswerValue(detail.getAnswerValue());
            return d;
        }).collect(Collectors.toList());
        dto.setAnswers(details);
        return dto;
    }
} 