package fc.icd.baulonboarding.survey.service;

import fc.icd.baulonboarding.survey.model.dto.SurveyDto;

public interface SurveyService {

    void registerSurvey(SurveyDto.RegisterSurveyRequest request);
}
