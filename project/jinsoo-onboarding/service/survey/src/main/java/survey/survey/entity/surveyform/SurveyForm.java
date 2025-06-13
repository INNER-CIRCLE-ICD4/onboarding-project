package survey.survey.entity.surveyform;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import survey.survey.entity.surveyquestion.SurveyQuestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "survey_form")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SurveyForm {
    @EmbeddedId
    private SurveyFormId surveyFormId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long surveyId;

    @OneToMany(mappedBy = "surveyForm", cascade = CascadeType.ALL)
    private List<SurveyQuestion> surveyQuestionList = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static SurveyForm create(SurveyFormId surveyFormId, String title, String description, Long surveyId) {
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.surveyFormId = surveyFormId;
        surveyForm.title = title;
        surveyForm.description = description;
        surveyForm.surveyId = surveyId;
        surveyForm.createdAt = LocalDateTime.now();
        surveyForm.modifiedAt = surveyForm.createdAt;
        return surveyForm;
    }

    public void addQuestion(SurveyQuestion surveyQuestion) {
        if (surveyQuestion == null || this.surveyQuestionList.contains(surveyQuestion)) {
            return;
        }

        addQuestionInternal(surveyQuestion);
    }

    public void addAllQuestions(List<SurveyQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            return;
        }

        questions.stream()
                .filter(Objects::nonNull)
                .filter(question -> !this.surveyQuestionList.contains(question))
                .forEach(this::addQuestionInternal);
    }

    private void addQuestionInternal(SurveyQuestion surveyQuestion) {
        this.surveyQuestionList.add(surveyQuestion);
        surveyQuestion.assignSurveyForm(this);
    }


}
