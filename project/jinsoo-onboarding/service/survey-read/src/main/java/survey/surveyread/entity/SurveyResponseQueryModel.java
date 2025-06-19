package survey.surveyread.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "survey_response")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SurveyResponseQueryModel extends BaseEntity {
    private Long surveyId;
    private Long surveyFormId;
    private Long surveySubmitId;

    @Lob
    private String surveyResponse;

    public static SurveyResponseQueryModel create(Long surveyId, Long surveyFormId,
                                                  Long surveySubmitId, String surveyResponse) {
        SurveyResponseQueryModel surveyResponseQueryModel = new SurveyResponseQueryModel();
        surveyResponseQueryModel.surveyId = surveyId;
        surveyResponseQueryModel.surveyFormId = surveyFormId;
        surveyResponseQueryModel.surveySubmitId = surveySubmitId;
        surveyResponseQueryModel.surveyResponse = surveyResponse;
        return surveyResponseQueryModel;
    }
}
