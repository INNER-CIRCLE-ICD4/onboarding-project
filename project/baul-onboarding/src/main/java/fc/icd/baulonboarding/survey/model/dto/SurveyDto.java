package fc.icd.baulonboarding.survey.model.dto;


import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SurveyDto {

    @Getter
    @Setter
    public static class RegisterSurveyRequest{

        private String name;

        private String description;

        private List<RegisterSurveyItemRequest> surveyItemList;

        public Survey toEntity(){
            return Survey.builder()
                    .name(name)
                    .description(description)
                    .build();
        }

    }


    @Getter
    @Setter
    public static class RegisterSurveyItemRequest{

        private String name;

        private String description;

        private InputType inputType;

        private boolean isRequired;

        private Integer ordering;

        private List<RegisterSurveyItemOptionRequest> surveyItemOptionList;

        public SurveyItem toEntity(Survey survey){
            return SurveyItem.builder()
                    .survey(survey)
                    .name(name)
                    .description(description)
                    .inputType(inputType)
                    .isRequired(isRequired)
                    .ordering(ordering)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class RegisterSurveyItemOptionRequest{

        private String content;

        private Integer ordering;

        public SurveyItemOption toEntity(SurveyItem surveyItem){
            return SurveyItemOption.builder()
                    .surveyItem(surveyItem)
                    .content(content)
                    .ordering(ordering)
                    .build();
        }

    }

    @Getter
    @Setter
    public static class UpdateSurveyRequest{

        private Long id;

        private String name;

        private String description;

        private List<UpdateSurveyItemRequest> surveyItemList;

    }

    @Getter
    @Setter
    public static class UpdateSurveyItemRequest{

        private Long id;

        private String name;

        private String description;

        private InputType inputType;

        private boolean isRequired;

        private boolean isDeleted;

        private Integer ordering;

        private List<UpdateSurveyItemOptionRequest> surveyItemOptionList;

        public SurveyItem toEntity(Survey survey){
            return SurveyItem.builder()
                    .survey(survey)
                    .name(name)
                    .description(description)
                    .inputType(inputType)
                    .isRequired(isRequired)
                    .ordering(ordering)
                    .build();
        }

    }

    @Getter
    @Setter
    public static class UpdateSurveyItemOptionRequest{

        private Long id;

        private String content;

        private Integer ordering;

        private boolean isDeleted;

        public SurveyItemOption toEntity(SurveyItem surveyItem){
            return SurveyItemOption.builder()
                    .surveyItem(surveyItem)
                    .content(content)
                    .ordering(ordering)
                    .build();
        }

    }


}


