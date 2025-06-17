package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;

import java.util.List;
import java.util.stream.Collectors;

public class AnswerToResponseDtoMapper {

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
