package survey.surveyread.service.response;

import java.util.List;

public record SurveyReadResponse(
        Long surveyFormId,
        Long surveySubmitId,
        List<QuestionReadResponse> surveyResponseList
) {
}
