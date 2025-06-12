package fc.icd.baulonboarding.survey.service;

import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import fc.icd.baulonboarding.survey.repository.store.SurveyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService{

    private final SurveyStore surveyStore;

    @Override
    @Transactional
    public void registerSurvey(SurveyDto.RegisterSurveyRequest request) {
        Survey survey = request.toEntity();
        List<SurveyItem> surveyItemList = new ArrayList<>();
        List<SurveyItemOption>  surveyItemOptionList = new ArrayList<>();

        request.getSurveyItemList().forEach(requestSurveyItem ->{
            SurveyItem surveyItem = requestSurveyItem.toEntity(survey);
            surveyItemList.add(surveyItem);
            requestSurveyItem.getSurveyItemOptionList().forEach(requestSurveyItemOption->{
                surveyItemOptionList.add(requestSurveyItemOption.toEntity(surveyItem));
            });
        });

        surveyStore.storeSurvey(survey);
        surveyStore.storeAllSurveyItems(surveyItemList);
        surveyStore.storeAllSurveyItemOptions(surveyItemOptionList);
    }
}
