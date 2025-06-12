package survey.survey.entity.surveyquestion;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import survey.survey.entity.surveyform.SurveyForm;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion {
    @Id
    private Long questionId;

    private int questionIndex;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private InputType inputType;
    private boolean required;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "survey_form_id", referencedColumnName = "surveyFormId"),
            @JoinColumn(name = "version", referencedColumnName = "version")
    })
    private SurveyForm surveyForm;

    @ElementCollection
    @CollectionTable(
            name = "question_candidates",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @OrderBy("checkCandidateIndex")
    private List<CheckCandidate> candidates = new ArrayList<>();

    private SurveyQuestion(String name, String description, InputType inputType, boolean required) {
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.required = required;
    }

    public static SurveyQuestion create(Long questionId, int index, String name, String description,
                                        InputType inputType,
                                        boolean required) {
        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.questionId = questionId;
        surveyQuestion.questionIndex = index;
        surveyQuestion.name = name;
        surveyQuestion.description = description;
        surveyQuestion.inputType = inputType;
        surveyQuestion.required = required;
        surveyQuestion.deleted = false;
        return surveyQuestion;
    }

    public void addCandidate(CheckCandidate candidate) {
        this.candidates.add(candidate);
    }

    public void assignSurveyForm(SurveyForm surveyForm) {
        this.surveyForm = surveyForm;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }
}