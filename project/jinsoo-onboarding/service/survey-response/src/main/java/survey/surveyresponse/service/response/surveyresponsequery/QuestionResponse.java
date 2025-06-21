package survey.surveyresponse.service.response.surveyresponsequery;

public record QuestionResponse(
        Long questionId,
        Long questionSubmitId,
        String answer
) {}

