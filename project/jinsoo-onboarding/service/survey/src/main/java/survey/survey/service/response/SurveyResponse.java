package survey.survey.service.response;

import lombok.Getter;
import survey.survey.entity.Survey;

import java.time.LocalDateTime;

@Getter
public class SurveyResponse {
    private final Long surveyId;
    private final Long surveyFormVersion;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private SurveyResponse(Survey surveyForm) {
        this.surveyId = surveyForm.getId();
        this.surveyFormVersion = surveyForm.getSurveyFormVersion();
        this.createdAt = surveyForm.getCreatedAt();
        this.modifiedAt = surveyForm.getModifiedAt();
    }

    public static SurveyResponse from(Survey survey) {
        return new SurveyResponse(survey);
    }
}