package fastcampus.inguk_onboarding.form.response.repository.entity.response;

import com.google.gson.Gson;
import fastcampus.inguk_onboarding.common.repository.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="survey_answer")
@Getter
@Setter
public class SurveyAnswerEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="survey_response_id")
    private SurveyResponseEntity surveyResponse;

    private Integer itemOrder;
    private String answer;

    public void setSurveyResponse(SurveyResponseEntity surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    public void setAnswer(Object answer) {
        if(answer instanceof List<?>) {
            this.answer = new Gson().toJson(answer);
        }else{
            this.answer = String.valueOf(answer);
        }
    }
}
