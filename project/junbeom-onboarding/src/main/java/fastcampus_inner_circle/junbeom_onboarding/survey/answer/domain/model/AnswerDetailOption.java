package fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerDetailOption {
    private Long id;
    private Long optionId;
    private String text;
}
