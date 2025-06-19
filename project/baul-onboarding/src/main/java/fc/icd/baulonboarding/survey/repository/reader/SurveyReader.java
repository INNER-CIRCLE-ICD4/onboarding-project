package fc.icd.baulonboarding.survey.repository.reader;

import fc.icd.baulonboarding.survey.model.entity.Survey;

public interface SurveyReader {

    Survey getSurveyBy(Long id);

}
