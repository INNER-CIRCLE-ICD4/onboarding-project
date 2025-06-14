package fc.icd.baulonboarding.answer.model.entity;

import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_answer_options")
public class SurveyAnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private SurveyAnswer surveyAnswer;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private SurveyItemOption surveyItemOption;

    private String optionText;


}
