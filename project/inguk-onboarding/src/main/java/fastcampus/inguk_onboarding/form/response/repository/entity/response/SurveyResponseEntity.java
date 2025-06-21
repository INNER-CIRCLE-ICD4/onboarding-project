package fastcampus.inguk_onboarding.form.response.repository.entity.response;


import fastcampus.inguk_onboarding.common.repository.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_response")
@Getter
@Setter
public class SurveyResponseEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long surveyId;
    private Long surveyVersionId;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SurveyAnswerEntity> answers = new ArrayList<>();

    public void addAnswer(SurveyAnswerEntity answer) {
        answers.add(answer);
        answer.setSurveyResponse(this);
    }
}
