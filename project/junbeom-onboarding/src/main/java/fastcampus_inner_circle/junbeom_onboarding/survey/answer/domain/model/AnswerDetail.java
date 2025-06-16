package fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDetail {
    private Long id;
    private Long contentId;
    private Long optionId;
    private String answerValue;
} 