package fc.icd.baulonboarding.survey.model.dto;


import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.common.validation.annotation.ValidItemOptions;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SurveyDto {

    @Getter
    @Setter
    public static class RegisterSurveyRequest{
        @NotBlank(message = "surveyName 는 필수값입니다")
        private String name;

        @NotBlank(message = "surveyDescription 는 필수값입니다")
        private String description;

        @Valid
        @NotEmpty(message = "surveyItemList 는 최소 1개 이상 있어야 합니다")
        private List<RegisterSurveyItemRequest> surveyItemList;
    }


    @Getter
    @Setter
    @ValidItemOptions(inputTypeField = "inputType", optionListField = "surveyItemOptionList")
    public static class RegisterSurveyItemRequest{
        @NotBlank(message = "surveyItemName 는 필수값입니다")
        private String name;

        @NotBlank(message = "surveyItemDescription 는 필수값입니다")
        private String description;

        @NotNull(message = "surveyItemInputType 는 필수값입니다")
        private InputType inputType;

        @NotNull(message = "surveyItemIsRequired 는 필수값입니다")
        private Boolean isRequired;

        @NotNull(message = "surveyItemOrdering 는 필수값입니다")
        @Min(value = 1, message = "surveyItemOrdering 는 1 이상이어야 합니다")
        private Integer ordering;

        @Valid
        private List<RegisterSurveyItemOptionRequest> surveyItemOptionList;
    }

    @Getter
    @Setter
    public static class RegisterSurveyItemOptionRequest{
        @NotBlank(message = "surveyItemOptionContent 는 필수값입니다")
        private String content;

        @NotNull(message = "surveyItemOptionOrdering 는 필수값입니다")
        @Min(value = 1, message = "surveyItemOptionOrdering 는 1 이상이어야 합니다")
        private Integer ordering;

    }

    @Getter
    @Setter
    public static class UpdateSurveyRequest{
        @NotNull(message = "surveyId 는 필수값입니다")
        private Long id;

        @NotBlank(message = "surveyName 는 필수값입니다")
        private String name;

        @NotBlank(message = "surveyDescription 는 필수값입니다")
        private String description;

        @Valid
        @NotEmpty(message = "surveyItemList 는 최소 1개 이상 있어야 합니다")
        private List<UpdateSurveyItemRequest> surveyItemList;

    }

    @Getter
    @Setter
    @ValidItemOptions(inputTypeField = "inputType", optionListField = "surveyItemOptionList")
    public static class UpdateSurveyItemRequest{
        @NotNull(message = "surveyItemId 는 필수값입니다")
        private Long id;

        @NotBlank(message = "surveyItemName 는 필수값입니다")
        private String name;

        @NotBlank(message = "surveyItemDescription 는 필수값입니다")
        private String description;

        @NotNull(message = "surveyItemInputType 는 필수값입니다")
        private InputType inputType;

        @NotNull(message = "surveyItemIsRequired 는 필수값입니다")
        private Boolean isRequired;

        @NotNull(message = "surveyItemIsDeleted 는 필수값입니다")
        private Boolean isDeleted;

        @NotNull(message = "surveyItemOrdering 는 필수값입니다")
        @Min(value = 1, message = "surveyItemOrdering 는 1 이상이어야 합니다")
        private Integer ordering;

        @Valid
        private List<UpdateSurveyItemOptionRequest> surveyItemOptionList;
    }

    @Getter
    @Setter
    public static class UpdateSurveyItemOptionRequest{
        @NotNull(message = "surveyItemOptionId 는 필수값입니다")
        private Long id;

        @NotBlank(message = "surveyItemOptionContent 는 필수값입니다")
        private String content;

        @NotNull(message = "surveyItemOptionOrdering 는 필수값입니다")
        @Min(value = 1, message = "surveyItemOptionOrdering 는 1 이상이어야 합니다")
        private Integer ordering;

        @NotNull(message = "surveyItemOptionIsDeleted 는 필수값입니다")
        private Boolean isDeleted;
    }


}


