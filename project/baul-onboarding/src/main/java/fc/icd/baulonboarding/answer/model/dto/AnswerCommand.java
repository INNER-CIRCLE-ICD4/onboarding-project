package fc.icd.baulonboarding.answer.model.dto;

import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.model.entity.AnswerItem;
import fc.icd.baulonboarding.answer.model.entity.AnswerItemOption;
import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AnswerCommand {

    @Getter
    @Setter
    public static class RegisterAnswer{
        private Long surveyId;
        private List<AnswerCommand.RegisterAnswerItem> answerItemList;

        public Answer toEntity(Survey survey){
            return Answer.builder()
                    .survey(survey)
                    .build();
        }

    }

    @Getter
    @Setter
    public static class RegisterAnswerItem{
        private Long surveyItemId;
        private InputType inputType;
        private String content;
        private List<AnswerCommand.RegisterAnswerItemOption> answerItemOptionList;

        public AnswerItem toEntity(Answer answer, SurveyItem surveyItem){
            return AnswerItem.builder()
                    .answer(answer)
                    .surveyItem(surveyItem)
                    .question(surveyItem.getName())
                    .inputType(surveyItem.getInputType())
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class RegisterAnswerItemOption{
        private Long optionId;
        public AnswerItemOption toEntity(AnswerItem answerItem, SurveyItemOption surveyItemOption){
            return AnswerItemOption.builder()
                    .answerItem(answerItem)
                    .surveyItemOption(surveyItemOption)
                    .content(surveyItemOption.getContent())
                    .build();
        }

    }
}
