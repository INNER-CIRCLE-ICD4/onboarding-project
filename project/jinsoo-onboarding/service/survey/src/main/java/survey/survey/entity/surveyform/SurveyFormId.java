package survey.survey.entity.surveyform;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class SurveyFormId implements Serializable {
    @Column(nullable = false)
    private Long surveyFormId;
    @Column(nullable = false)
    private Long version = 1L;

    public SurveyFormId() {
    }

    private SurveyFormId(Long surveyFormId, Long version) {
        this.surveyFormId = surveyFormId;
        this.version = version;
    }

    public static SurveyFormId create(Long surveyFormId) {
        return new SurveyFormId(surveyFormId,1L);
    }

    public static SurveyFormId update(Long surveyFormId, Long version) {
        return new SurveyFormId(surveyFormId, version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyFormId that)) return false;
        return Objects.equals(surveyFormId, that.surveyFormId)
                && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyFormId, version);
    }
}
