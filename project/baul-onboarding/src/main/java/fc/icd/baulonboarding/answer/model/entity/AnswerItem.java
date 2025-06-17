package fc.icd.baulonboarding.answer.model.entity;

import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answer_items")
public class AnswerItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "survey_item_id")
    private SurveyItem surveyItem;

    private String question;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "answerItem", cascade = CascadeType.PERSIST)
    private List<AnswerItemOption> answerItemOptionList = new ArrayList<>();

    @Builder
    public AnswerItem(Answer answer,
                      SurveyItem surveyItem,
                      String question,
                      InputType inputType,
                      String content
    ) {
        this.answer = answer;
        this.surveyItem = surveyItem;
        this.question = question;
        this.inputType = inputType;
        this.content = content;
    }

    public boolean isSelectableType(){
        return inputType == InputType.SINGLE_CHOICE || inputType == InputType.MULTI_CHOICE;
    }

}
