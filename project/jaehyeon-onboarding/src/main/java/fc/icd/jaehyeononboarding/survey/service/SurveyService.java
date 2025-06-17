package fc.icd.jaehyeononboarding.survey.service;

import fc.icd.jaehyeononboarding.common.model.NoDataResponse;
import fc.icd.jaehyeononboarding.survey.model.dto.SurveyCreateDTO;
import fc.icd.jaehyeononboarding.survey.model.dto.SurveyUpdateDTO;

public interface SurveyService {
    NoDataResponse createSurvey(SurveyCreateDTO createDTO);
    NoDataResponse updateSurvey(SurveyUpdateDTO updateDTO);
}
