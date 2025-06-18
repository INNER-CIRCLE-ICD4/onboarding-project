package survey.surveyresponse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "survey_submit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SurveySubmit extends BaseEntity {

    private Long surveyId;
    private Long surveyFormId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static SurveySubmit create(Long surveyId, Long surveyFormId) {
        SurveySubmit surveySubmit = new SurveySubmit();
        surveySubmit.surveyId = surveyId;
        surveySubmit.surveyFormId = surveyFormId;
        surveySubmit.createdAt = LocalDateTime.now();
        surveySubmit.modifiedAt =  surveySubmit.createdAt;
        return surveySubmit;
    }
}
