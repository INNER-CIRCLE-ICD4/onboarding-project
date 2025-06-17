package survey.survey.entity.surveyquestion;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import survey.survey.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "survey_questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion extends BaseEntity {
    private int questionIndex;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    private boolean required;

    private boolean deleted;

    private Long surveyFormId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @ElementCollection
    @CollectionTable(
            name = "question_candidates",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @OrderColumn(name = "index")
//    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    private List<CheckCandidate> candidates = new ArrayList<>();

    public static SurveyQuestion create(
                                        int index,
                                        String name,
                                        String description,
                                        InputType inputType,
                                        boolean required) {
        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.questionIndex = index;
        surveyQuestion.name = name;
        surveyQuestion.description = description;
        surveyQuestion.inputType = inputType;
        surveyQuestion.required = required;
        surveyQuestion.deleted = false;
        surveyQuestion.createdAt = LocalDateTime.now();
        surveyQuestion.modifiedAt = surveyQuestion.createdAt;
        return surveyQuestion;
    }

    public void update(String name, int index, String description, InputType inputType, boolean required) {
        this.name = name;
        this.questionIndex = index;
        this.description = description;
        this.inputType = inputType;
        this.required = required;
    }

    public void addCandidates(List<CheckCandidate> candidates) {
        this.inputType.validateQuestion(this, candidates);

        if (this.candidates == null) {
            this.candidates = new ArrayList<>();
        }
        if (candidates != null) {
            this.candidates.addAll(candidates);
        }
    }

    public void updateCandidates(List<CheckCandidate> newCandidates) {
        this.inputType.validateQuestion(this, newCandidates);

        List<CheckCandidate> safeNewCandidates = newCandidates != null ? newCandidates : new ArrayList<>();

        boolean changed = this.candidates.size() != safeNewCandidates.size() ||
                !new HashSet<>(this.candidates).equals(new HashSet<>(safeNewCandidates));

        if (changed) {
            this.candidates.clear();
            this.candidates.addAll(safeNewCandidates);
            this.modifiedAt = LocalDateTime.now();
        }
    }

    public void delete() {
        this.deleted = true;
        this.modifiedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deleted = false;
        this.modifiedAt = LocalDateTime.now();
    }
}