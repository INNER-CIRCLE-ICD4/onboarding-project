package fc.icd.baulonboarding.answer.model.entity;

import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_answers")
public class SurveyAnswer extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "response_id")
    private SurveyResponse surveyResponse;

    @ManyToOne
    @JoinColumn(name = "survey_item_id")
    private SurveyItem surveyItem;

    private String questionText;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    private String answerText;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyAnswer", cascade = CascadeType.PERSIST)
    private List<SurveyAnswerOption> surveyAnswerOptionList = new ArrayList<>();

}
