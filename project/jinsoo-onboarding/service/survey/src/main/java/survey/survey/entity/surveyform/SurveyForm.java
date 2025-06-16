package survey.survey.entity.surveyform;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "survey_form")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SurveyForm {
    @Id
    private Long surveyFormId;

    @Version
    private Long version;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long surveyId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static SurveyForm create(Long surveyFormId, String title, String description, Long surveyId) {
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.surveyFormId = surveyFormId;
        surveyForm.title = title;
        surveyForm.description = description;
        surveyForm.surveyId = surveyId;
        surveyForm.createdAt = LocalDateTime.now();
        surveyForm.modifiedAt = surveyForm.createdAt;
        return surveyForm;
    }

    public void update(String title, String description, Long surveyId) {
        this.title = title;
        this.description = description;
        this.surveyId = surveyId;
    }

    public void incrementVersion() {
        if (this.version == null) {
            this.version = 1L;
        } else {
            this.version++;
        }
        this.modifiedAt = LocalDateTime.now();
    }

}
