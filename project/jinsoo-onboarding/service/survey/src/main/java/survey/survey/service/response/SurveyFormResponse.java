package survey.survey.service.response;

import lombok.Getter;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.InputType;
import survey.survey.entity.surveyquestion.SurveyQuestion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SurveyFormResponse {
    private final Long surveyFormId;
    private final Long version;
    private final Long surveyId;
    private final String title;
    private final String description;
    private final List<QuestionResponse> questions;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private SurveyFormResponse(SurveyForm surveyForm) {
        this.surveyFormId = surveyForm.getSurveyFormId().getSurveyFormId();
        this.version = surveyForm.getSurveyFormId().getVersion();
        this.surveyId = surveyForm.getSurveyId();
        this.title = surveyForm.getTitle();
        this.description = surveyForm.getDescription();
        this.questions = surveyForm.getSurveyQuestionList().stream()
                .map(QuestionResponse::from)
                .collect(Collectors.toList());
        this.createdAt = surveyForm.getCreatedAt();
        this.modifiedAt = surveyForm.getModifiedAt();
    }

    public static SurveyFormResponse from(SurveyForm surveyForm) {
        return new SurveyFormResponse(surveyForm);
    }

    @Getter
    public static class QuestionResponse {
        private final Long questionId;
        private final int questionIndex;
        private final String name;
        private final String description;
        private final InputType inputType;
        private final boolean required;
        private final List<CheckCandidate> candidates;

        private QuestionResponse(SurveyQuestion question) {
            this.questionId = question.getQuestionId();
            this.questionIndex = question.getQuestionIndex();
            this.name = question.getName();
            this.description = question.getDescription();
            this.inputType = question.getInputType();
            this.required = question.isRequired();
            this.candidates = question.getCandidates().stream().toList();
        }

        public static QuestionResponse from(SurveyQuestion question) {
            return new QuestionResponse(question);
        }
    }
}