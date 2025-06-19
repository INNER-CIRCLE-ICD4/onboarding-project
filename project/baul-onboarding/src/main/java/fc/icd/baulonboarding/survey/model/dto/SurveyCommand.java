package fc.icd.baulonboarding.survey.model.dto;

import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SurveyCommand {

    @Getter
    @Setter
    public static class RegisterSurvey{
        private String name;
        private String description;
        private List<SurveyCommand.RegisterSurveyItem> surveyItemList;

        public Survey toEntity(){
            return Survey.builder()
                    .name(name)
                    .description(description)
                    .build();
        }

    }


    @Getter
    @Setter
    public static class RegisterSurveyItem{
        private String name;
        private String description;
        private InputType inputType;
        private Boolean isRequired;
        private Integer ordering;
        private List<SurveyCommand.RegisterSurveyItemOption> surveyItemOptionList;

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
    public static class RegisterSurveyItemOption{
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
    public static class UpdateSurvey{
        private Long id;
        private String name;
        private String description;
        private List<SurveyCommand.UpdateSurveyItem> surveyItemList;
    }

    @Getter
    @Setter
    public static class UpdateSurveyItem{
        private Long id;
        private String name;
        private String description;
        private InputType inputType;
        private Boolean isRequired;
        private Boolean isDeleted;
        private Integer ordering;
        private List<SurveyCommand.UpdateSurveyItemOption> surveyItemOptionList;

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
    public static class UpdateSurveyItemOption{
        private Long id;
        private String content;
        private Integer ordering;
        private Boolean isDeleted;

        public SurveyItemOption toEntity(SurveyItem surveyItem){
            return SurveyItemOption.builder()
                    .surveyItem(surveyItem)
                    .content(content)
                    .ordering(ordering)
                    .build();
        }

    }
}
