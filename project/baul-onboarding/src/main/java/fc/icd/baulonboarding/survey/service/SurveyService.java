package fc.icd.baulonboarding.survey.service;

import fc.icd.baulonboarding.survey.model.dto.SurveyCommand;

public interface SurveyService {

    void registerSurvey(SurveyCommand.RegisterSurvey registerSurvey);

    void updateSurvey(SurveyCommand.UpdateSurvey updateSurvey);
}
