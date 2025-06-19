package fc.icd.baulonboarding.answer.service;

import fc.icd.baulonboarding.answer.model.dto.AnswerCommand;

import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;
import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.model.entity.AnswerItem;
import fc.icd.baulonboarding.answer.model.entity.AnswerItemOption;
import fc.icd.baulonboarding.answer.model.mapper.AnswerInfoMapper;
import fc.icd.baulonboarding.answer.repository.reader.AnswerReader;
import fc.icd.baulonboarding.answer.repository.store.AnswerStore;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import fc.icd.baulonboarding.survey.repository.reader.SurveyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{

    private final AnswerStore answerStore;
    private final SurveyReader surveyReader;
    private final AnswerReader answerReader;
    private final AnswerInfoMapper answerInfoMapper;

    @Override
    public void registerAnswer(AnswerCommand.RegisterAnswer registerAnswer) {
        Survey survey = surveyReader.getSurveyBy(registerAnswer.getSurveyId());
        Answer answer = registerAnswer.toEntity(survey);

        Map<Long, SurveyItem> existingSurveyItemMap = survey.getSurveyItemList().stream()
                .collect(Collectors.toMap(SurveyItem::getId, item -> item));

        registerAnswer.getAnswerItemList().forEach(requestAnswerItem ->{
            SurveyItem surveyItem = existingSurveyItemMap.get(requestAnswerItem.getSurveyItemId());
            AnswerItem answerItem = requestAnswerItem.toEntity(answer, surveyItem);
            answer.getAnswerItemList().add(answerItem);

            Map<Long, SurveyItemOption> existingSurveyItemOptionMap = surveyItem.getSurveyItemOptionList().stream()
                    .collect(Collectors.toMap(SurveyItemOption::getId, option -> option));

            if(answerItem.isSelectableType()){
                requestAnswerItem.getAnswerItemOptionList().forEach(requestAnswerItemOption ->{
                    SurveyItemOption surveyItemOption = existingSurveyItemOptionMap.get(requestAnswerItemOption.getOptionId());
                    AnswerItemOption answerItemOption = requestAnswerItemOption.toEntity(answerItem, surveyItemOption);
                    answerItem.getAnswerItemOptionList().add(answerItemOption);
                });
            }

        });

        answerStore.store(answer);
    }

    @Override
    public AnswerInfo.Answer retrieveAnswer(Long answerId) {
        Answer answer = answerReader.getAnswerBy(answerId);
        return answerInfoMapper.of(answer);
    }
}
