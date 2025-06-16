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

    // 테스트용 생성자 추가
    private SurveyFormResponse(Long surveyFormId, Long version, Long surveyId, String title,
                               String description, List<QuestionResponse> questions,
                               LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.surveyFormId = surveyFormId;
        this.version = version;
        this.surveyId = surveyId;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static SurveyFormResponse from(SurveyForm surveyForm) {
        return new SurveyFormResponse(surveyForm);
    }

    // 테스트용 팩토리 메서드 추가
    public static SurveyFormResponse of(Long surveyFormId, Long version, Long surveyId,
                                        String title, String description,
                                        List<QuestionResponse> questions,
                                        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new SurveyFormResponse(surveyFormId, version, surveyId, title, description,
                questions, createdAt, modifiedAt);
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

        // 테스트용 생성자 추가
        private QuestionResponse(Long questionId, int questionIndex, String name, String description,
                                 InputType inputType, boolean required, List<CheckCandidate> candidates) {
            this.questionId = questionId;
            this.questionIndex = questionIndex;
            this.name = name;
            this.description = description;
            this.inputType = inputType;
            this.required = required;
            this.candidates = candidates;
        }

        public static QuestionResponse from(SurveyQuestion question) {
            return new QuestionResponse(question);
        }

        // 테스트용 팩토리 메서드 추가
        public static QuestionResponse of(Long questionId, int questionIndex, String name,
                                          String description, InputType inputType, boolean required,
                                          List<CheckCandidate> candidates) {
            return new QuestionResponse(questionId, questionIndex, name, description, inputType,
                    required, candidates);
        }
    }
}