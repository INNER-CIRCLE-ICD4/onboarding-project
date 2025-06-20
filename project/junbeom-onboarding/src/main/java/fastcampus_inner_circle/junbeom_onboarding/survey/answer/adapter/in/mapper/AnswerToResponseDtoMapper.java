package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;

public class AnswerToResponseDtoMapper {

    // 도메인 -> DTO
   public static AnswerResponse toResponse(Answer domain) {
       return AnswerResponse.builder()
               .answerId(domain.getId())
               .formId(domain.getFormId())
               .build();
   }
}
