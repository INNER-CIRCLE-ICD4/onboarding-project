package fc.icd.baulonboarding.survey.repository.store;

import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;

import java.util.List;

public interface SurveyStore {

    Survey storeSurvey(Survey survey);

    void storeAllSurveyItems(List<SurveyItem> surveyItemList);

    void storeAllSurveyItemOptions(List<SurveyItemOption> surveyItemOptionList);

}
