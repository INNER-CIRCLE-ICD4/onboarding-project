package survey.survey.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "survey")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Survey {
    @Id
    private Long surveyId;

    private Long surveyFormId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Survey create(Long surveyId, Long surveyFormId, LocalDateTime startAt, LocalDateTime endAt) {
        Survey survey = new Survey();
        survey.surveyId = surveyId;
        survey.surveyFormId = surveyFormId;
        survey.startAt = startAt;
        survey.endAt = endAt;
        survey.createdAt = LocalDateTime.now();
        survey.modifiedAt = survey.createdAt;
        return survey;
    }
}
