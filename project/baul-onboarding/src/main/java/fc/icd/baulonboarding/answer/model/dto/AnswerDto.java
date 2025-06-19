package fc.icd.baulonboarding.answer.model.dto;

import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.model.entity.AnswerItem;
import fc.icd.baulonboarding.answer.model.entity.AnswerItemOption;
import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.common.validation.annotation.ValidItemOptions;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AnswerDto {

    @Getter
    @Setter
    public static class RegisterAnswerRequest{

        @NotNull(message = "surveyId 는 필수값입니다")
        private Long surveyId;

        @Valid
        @NotEmpty(message = "surveyItemList 는 최소 1개 이상 있어야 합니다")
        private List<AnswerDto.RegisterAnswerItemRequest> answerItemList;

        public Answer toEntity(Survey survey){
            return Answer.builder()
                    .survey(survey)
                    .build();
        }

    }

    @Getter
    @Setter
    @ValidItemOptions(inputTypeField = "inputType", optionListField = "answerItemOptionList")
    public static class RegisterAnswerItemRequest{

        @NotNull(message = "surveyItemId 는 필수값입니다")
        private Long surveyItemId;

        @NotNull(message = "surveyItemInputType 는 필수값입니다")
        private InputType inputType;

        private String content;

        @Valid
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

        @NotNull(message = "optionId 는 필수값입니다")
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
