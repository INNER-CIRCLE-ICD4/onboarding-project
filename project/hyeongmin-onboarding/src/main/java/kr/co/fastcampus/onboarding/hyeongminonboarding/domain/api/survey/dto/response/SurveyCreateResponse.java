package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyCreateResponse {
    private long surveyId;

}
