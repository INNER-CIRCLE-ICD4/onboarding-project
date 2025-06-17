package survey.survey.entity.surveyform;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import survey.survey.entity.BaseEntity;

import java.time.LocalDateTime;

@Table(name = "survey_form")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SurveyForm extends BaseEntity {
    private Long version;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private Long surveyId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static SurveyForm create(Long version, String title, String description, Long surveyId) {
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.version = version;
        surveyForm.title = title;
        surveyForm.description = description;
        surveyForm.surveyId = surveyId;
        surveyForm.createdAt = LocalDateTime.now();
        surveyForm.modifiedAt = surveyForm.createdAt;
        return surveyForm;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void incrementVersion() {
        this.version++;
    }
}
