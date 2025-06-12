package fc.icd.baulonboarding.survey.repository.store;

import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import fc.icd.baulonboarding.survey.repository.entity.SurveyItemOptionRepository;
import fc.icd.baulonboarding.survey.repository.entity.SurveyItemRepository;
import fc.icd.baulonboarding.survey.repository.entity.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class SurveyStoreImpl implements SurveyStore{

    private final SurveyRepository surveyRepository;
    private final SurveyItemRepository surveyItemRepository;
    private final SurveyItemOptionRepository surveyItemOptionRepository;

    @Override
    public Survey storeSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    @Override
    public void storeAllSurveyItems(List<SurveyItem> surveyItemList) {
        surveyItemRepository.saveAll(surveyItemList);
    }

    @Override
    public void storeAllSurveyItemOptions(List<SurveyItemOption> surveyItemOptionList) {
        surveyItemOptionRepository.saveAll(surveyItemOptionList);
    }

}
