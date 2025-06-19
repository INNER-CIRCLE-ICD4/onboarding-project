package fc.icd.baulonboarding.survey.model.entity;

import fc.icd.baulonboarding.common.exception.InvalidParamException;
import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import fc.icd.baulonboarding.common.model.code.InputType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_items")
public class SurveyItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    private boolean isRequired;

    private boolean isDeleted;

    private Integer ordering;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyItem", cascade = CascadeType.PERSIST)
    private List<SurveyItemOption> surveyItemOptionList = new ArrayList<>();

    @Builder
    public SurveyItem(Survey survey,
                      String name,
                      String description,
                      InputType inputType,
                      Boolean isRequired,
                      Integer ordering
                      ){
        if (survey == null) throw new InvalidParamException("Entity SurveyItem.survey");
        if (!StringUtils.hasText(name)) throw new InvalidParamException("Entity SurveyItem.name");
        if (inputType == null) throw new InvalidParamException("Entity SurveyItem.inputType");
        if (isRequired == null) throw new InvalidParamException("Entity SurveyItem.isRequired");
        if (ordering == null) throw new InvalidParamException("Entity SurveyItem.ordering");

        this.survey = survey;
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.isRequired = isRequired;
        this.ordering = ordering;
    }

    public void applyChanges(
            String name,
            String description,
            InputType inputType,
            Boolean isRequired,
            Boolean isDeleted,
            Integer ordering
            ){

        if (!StringUtils.hasText(name)) throw new InvalidParamException("Entity SurveyItem.name");
        if (inputType == null) throw new InvalidParamException("Entity SurveyItem.inputType");
        if (isRequired == null) throw new InvalidParamException("Entity SurveyItem.isRequired");
        if (isDeleted == null) throw new InvalidParamException("Entity SurveyItem.isDeleted");
        if (ordering == null) throw new InvalidParamException("Entity SurveyItem.ordering");

        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.isRequired = isRequired;
        this.isDeleted = isDeleted;
        this.ordering = ordering;
    }

    public boolean isSelectableType(){
        return inputType == InputType.SINGLE_CHOICE || inputType == InputType.MULTI_CHOICE;
    }


}
