package survey.surveyresponse.service.response;

import java.util.List;

public record SurveySubmitResponse(
        Long surveySubmitId,
        List<SurveyQuestionSubmitResponse> surveyQuestionSubmitResponses) {

    public static SurveySubmitResponse of(Long surveySubmitId, List<SurveyQuestionSubmitResponse> surveyQuestionSubmitResponses) {
        return new SurveySubmitResponse(surveySubmitId, surveyQuestionSubmitResponses);
    }
}
