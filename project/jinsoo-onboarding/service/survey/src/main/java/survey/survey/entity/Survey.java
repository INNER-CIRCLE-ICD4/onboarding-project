package survey.survey.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "survey")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Survey extends BaseEntity {

    private Long surveyFormVersion;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Survey create(Long surveyFormVersion) {
        Survey survey = new Survey();
        survey.surveyFormVersion = surveyFormVersion;
        survey.createdAt = LocalDateTime.now();
        survey.modifiedAt = survey.createdAt;
        return survey;
    }

    public void increaseVersion(Long surveyFormVersion) {
        this.surveyFormVersion = surveyFormVersion;
        this.modifiedAt = LocalDateTime.now();
    }
}
