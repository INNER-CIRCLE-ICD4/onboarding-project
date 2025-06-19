package fc.icd.baulonboarding.survey.model.entity;

import fc.icd.baulonboarding.common.exception.InvalidParamException;
import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_item_options")
public class SurveyItemOption extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_item_id")
    private SurveyItem surveyItem;

    private String content;

    private Integer ordering;

    private boolean isDeleted;

    @Builder
    public SurveyItemOption(SurveyItem surveyItem,
                            String content,
                            Integer ordering
    ){
        if(surveyItem == null) throw new InvalidParamException("Entity SurveyItemOption.surveyItem");
        if(!StringUtils.hasText(content)) throw new InvalidParamException("Entity SurveyItemOption.content");
        if(ordering == null) throw new InvalidParamException("Entity SurveyItemOption.ordering");

        this.surveyItem = surveyItem;
        this.content = content;
        this.ordering = ordering;
    }

    public void applyChanges(String content,
                        Integer ordering,
                        Boolean isDeleted
    ){
        if(!StringUtils.hasText(content)) throw new InvalidParamException("Entity SurveyItemOption.content");
        if(ordering == null) throw new InvalidParamException("Entity SurveyItemOption.ordering");
        if(isDeleted == null) throw new InvalidParamException("Entity SurveyItemOption.isDeleted");

        this.content = content;
        this.ordering = ordering;
        this.isDeleted = isDeleted;
    }

}
