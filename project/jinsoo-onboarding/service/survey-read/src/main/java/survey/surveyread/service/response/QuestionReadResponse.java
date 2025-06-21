package survey.surveyread.service.response;

public record QuestionReadResponse(
        Long questionId,
        Long questionSubmitId,
        String answer
) {}

