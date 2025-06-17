package fc.icd.baulonboarding.answer.model.dto;

import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.model.entity.AnswerItem;
import fc.icd.baulonboarding.answer.model.entity.AnswerItemOption;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AnswerDto {

    @Getter
    @Setter
    public static class RegisterAnswerRequest{

        private Long surveyId;

        private List<AnswerDto.RegisterAnswerItemRequest> answerItemList;

        public Answer toEntity(Survey survey){
            return Answer.builder()
                    .survey(survey)
                    .build();
        }

    }

    @Getter
    @Setter
    public static class RegisterAnswerItemRequest{

        private Long answerId;

        private Long surveyItemId;

        private String content;

        private List<AnswerDto.RegisterAnswerItemOptionRequest> answerItemOptionList;

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
    public static class RegisterAnswerItemOptionRequest{

        private Long answerItemId;

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
