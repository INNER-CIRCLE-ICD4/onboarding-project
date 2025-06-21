package onboarding.survey.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onboarding.survey.exception.BadRequestException;

@Entity
@Table(name = "survey_response")
@Getter @NoArgsConstructor
public class SurveyResponse {

    @Id @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private Long surveyId;

    @Getter
    private Long surveyItemId;

    @Getter
    private String answer;

    public SurveyResponse(Long surveyId, Long surveyItemId, String answer) {
        validateSurveyId(surveyId);
        validateSurveyItemId(surveyItemId);
        validateAnswer(answer);
        this.surveyId     = surveyId;
        this.surveyItemId = surveyItemId;
        this.answer       = answer;    }

    private void validateSurveyId(Long surveyId) {
        if (surveyId == null) {
            throw new BadRequestException("설문 ID는 필수입니다.");
        }
    }

    private void validateSurveyItemId(Long surveyItemId) {
        if (surveyItemId == null) {
            throw new BadRequestException("문항 ID는 필수입니다.");
        }
    }

    private void validateAnswer(String answer) {
        if (answer == null || answer.isBlank()) {
            throw new BadRequestException("답변은 빈 값일 수 없습니다.");
        }
    }
}