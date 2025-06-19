package survey.surveyresponse.service.response.surveyresponsequery;

import java.util.List;

public record SurveyResponse(
        Long surveyId,
        Long surveyFormId,
        Long surveySubmitId,
        List<QuestionResponse> surveyResponseList
) {}

