package fc.icd.baulonboarding.survey.service;

import fc.icd.baulonboarding.survey.model.dto.SurveyCommand;
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
    public void registerSurvey(SurveyCommand.RegisterSurvey registerSurvey) {
        Survey survey = registerSurvey.toEntity();

        registerSurvey.getSurveyItemList().forEach(requestSurveyItem -> {
            SurveyItem surveyItem = requestSurveyItem.toEntity(survey);
            survey.getSurveyItemList().add(surveyItem);

            if (surveyItem.isSelectableType()) {
                requestSurveyItem.getSurveyItemOptionList().forEach(requestSurveyItemOption -> {
                    SurveyItemOption option = requestSurveyItemOption.toEntity(surveyItem);
                    surveyItem.getSurveyItemOptionList().add(option);
                });
            }

        });

        surveyStore.store(survey);
    }

    @Override
    @Transactional
    public void updateSurvey(SurveyCommand.UpdateSurvey updateSurvey) {
        Survey survey = surveyReader.getSurveyBy(updateSurvey.getId());
        survey.applyChanges(updateSurvey.getName(), updateSurvey.getDescription());

        Map<Long, SurveyItem> existingItemMap = survey.getSurveyItemList().stream()
                .collect(Collectors.toMap(SurveyItem::getId, item -> item));

        for (SurveyCommand.UpdateSurveyItem requestSurveyItem : updateSurvey.getSurveyItemList()) {
            SurveyItem item = requestSurveyItem.getId() != null
                    ? existingItemMap.get(requestSurveyItem.getId()) : null;

            if (item != null) {
                item.applyChanges(
                        requestSurveyItem.getName(),
                        requestSurveyItem.getDescription(),
                        requestSurveyItem.getInputType(),
                        requestSurveyItem.getIsRequired(),
                        requestSurveyItem.getIsDeleted(),
                        requestSurveyItem.getOrdering()
                );
            } else {
                item = requestSurveyItem.toEntity(survey);
                survey.getSurveyItemList().add(item);
            }

            Map<Long, SurveyItemOption> existingOptionMap = item.getSurveyItemOptionList().stream()
                    .collect(Collectors.toMap(SurveyItemOption::getId, option -> option));

            for (SurveyCommand.UpdateSurveyItemOption requestSurveyItemOption : requestSurveyItem.getSurveyItemOptionList()) {
                SurveyItemOption option = requestSurveyItemOption.getId() != null
                        ? existingOptionMap.get(requestSurveyItemOption.getId()) : null;

                if (option != null) {
                    option.applyChanges(
                            requestSurveyItemOption.getContent(),
                            requestSurveyItemOption.getOrdering(),
                            requestSurveyItemOption.getIsDeleted()
                    );
                } else {
                    SurveyItemOption newOption = requestSurveyItemOption.toEntity(item);
                    item.getSurveyItemOptionList().add(newOption);
                }
            }
        }
    }
}
