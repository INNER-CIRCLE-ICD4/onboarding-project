package survey.surveyresponse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "survey_question_submit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SurveyQuestionSubmit extends BaseEntity {

    private Long questionId;

    private String answer;

    private Long surveySubmitId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static SurveyQuestionSubmit create(Long questionId, String answer, Long surveySubmitId) {
        SurveyQuestionSubmit surveyQuestionSubmit = new SurveyQuestionSubmit();
        surveyQuestionSubmit.questionId = questionId;
        surveyQuestionSubmit.answer = answer;
        surveyQuestionSubmit.surveySubmitId = surveySubmitId;
        surveyQuestionSubmit.createdAt = LocalDateTime.now();
        surveyQuestionSubmit.modifiedAt = surveyQuestionSubmit.createdAt;
        return surveyQuestionSubmit;
    }
}
