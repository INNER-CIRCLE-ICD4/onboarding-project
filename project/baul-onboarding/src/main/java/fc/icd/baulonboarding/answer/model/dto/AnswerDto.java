package fc.icd.baulonboarding.answer.model.dto;

import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.common.validation.annotation.ValidItemOptions;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
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

    }

    @Getter
    @Setter
    public static class RegisterAnswerItemOptionRequest{
        @NotNull(message = "optionId 는 필수값입니다")
        private Long optionId;
    }

    @Getter
    @Builder
    public static class Answer{
        private Long id;
        private Long surveyId;
        private String name;
        private String description;
        private List<AnswerInfo.AnswerItem> answerItemList;
    }

    @Getter
    @Builder
    public static class AnswerItem{
        private Long id;
        private Long answerId;
        private Long surveyItemId;
        private String question;
        private InputType inputType;
        private String content;
        private List<AnswerInfo.AnswerItemOption> answerItemOptionList;
    }

    @Getter
    @Builder
    public static class AnswerItemOption{
        private Long id;
        private Long answerItemId;
        private Long optionId;
        private String content;
    }


}
