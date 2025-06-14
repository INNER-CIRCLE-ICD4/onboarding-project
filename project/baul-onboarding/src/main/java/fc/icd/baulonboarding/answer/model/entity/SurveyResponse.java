package fc.icd.baulonboarding.answer.model.entity;

import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_responses")
public class SurveyResponse extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyResponse", cascade = CascadeType.PERSIST)
    private List<SurveyAnswer> surveyAnswerList = new ArrayList<>();

}
