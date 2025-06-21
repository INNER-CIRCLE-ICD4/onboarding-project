package survey.surveyresponse.controller.request;

import java.util.List;

public record SurveySubmitRequest(
        Long surveyFormId,
        List<SurveyQuestionSubmitRequest> surveyQuestionSubmitRequests) {

}
