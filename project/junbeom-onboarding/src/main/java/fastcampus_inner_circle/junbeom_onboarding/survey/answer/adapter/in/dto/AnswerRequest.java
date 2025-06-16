package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto;

import lombok.Data;
import java.util.List;

@Data
public class AnswerRequest {
    private Long formId;
    private List<AnswerDto> answers;

    @Data
    public static class AnswerDto {
        private Long contentId;
        private Long optionId; // 선택형일 때만 사용
        private String answerValue; // 주관식일 때만 사용
    }
} 