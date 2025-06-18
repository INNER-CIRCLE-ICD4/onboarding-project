package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnswerResponse {
    private Long answerId;
    private Long formId;
    private LocalDateTime submittedAt;
    private List<AnswerDetailDto> answers;

    @Data
    public static class AnswerDetailDto {
        private Long contentId;
        private List<Long> optionIds;
        private String answerValue;
    }
} 