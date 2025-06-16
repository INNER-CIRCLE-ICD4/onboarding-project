package fc.icd.baulonboarding.survey.service;

import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import fc.icd.baulonboarding.survey.repository.reader.SurveyReader;
import fc.icd.baulonboarding.survey.repository.store.SurveyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService{

    private final SurveyStore surveyStore;

    private final SurveyReader surveyReader;

    @Override
    @Transactional
    public void registerSurvey(SurveyDto.RegisterSurveyRequest request) {
        Survey survey = request.toEntity();

        request.getSurveyItemList().forEach(requestSurveyItem -> {
            SurveyItem surveyItem = requestSurveyItem.toEntity(survey);
            survey.getSurveyItemList().add(surveyItem);

            requestSurveyItem.getSurveyItemOptionList().forEach(requestSurveyItemOption -> {
                SurveyItemOption option = requestSurveyItemOption.toEntity(surveyItem);
                surveyItem.getSurveyItemOptionList().add(option);
            });

        });

        surveyStore.storeSurvey(survey);
    }

    @Override
    @Transactional
    public void updateSurvey(SurveyDto.UpdateSurveyRequest request) {
        Survey survey = surveyReader.getSurveyBy(request.getId());
        survey.applyChanges(request.getName(), request.getDescription());

        Map<Long, SurveyItem> existingItemMap = survey.getSurveyItemList().stream()
                .collect(Collectors.toMap(SurveyItem::getId, item -> item));

        for (SurveyDto.UpdateSurveyItemRequest requestSurveyItem : request.getSurveyItemList()) {
            SurveyItem item = requestSurveyItem.getId() != null ? existingItemMap.get(requestSurveyItem.getId()) : null;

            if (item != null) {
                item.applyChanges(
                        requestSurveyItem.getName(),
                        requestSurveyItem.getDescription(),
                        requestSurveyItem.getInputType(),
                        requestSurveyItem.isRequired(),
                        requestSurveyItem.isDeleted(),
                        requestSurveyItem.getOrdering()
                );
            } else {
                item = requestSurveyItem.toEntity(survey);
                survey.getSurveyItemList().add(item);
            }

            Map<Long, SurveyItemOption> existingOptions = item.getSurveyItemOptionList().stream()
                    .collect(Collectors.toMap(SurveyItemOption::getId, o -> o));

            for (SurveyDto.UpdateSurveyItemOptionRequest requestSurveyItemOption : requestSurveyItem.getSurveyItemOptionList()) {
                SurveyItemOption option = requestSurveyItemOption.getId() != null
                        ? existingOptions.get(requestSurveyItemOption.getId()) : null;

                if (option != null) {
                    option.applyChanges(
                            requestSurveyItemOption.getContent(),
                            requestSurveyItemOption.getOrdering(),
                            requestSurveyItemOption.isDeleted()
                    );
                } else {
                    SurveyItemOption newOption = requestSurveyItemOption.toEntity(item);
                    item.getSurveyItemOptionList().add(newOption);
                }
            }
        }
    }
}
