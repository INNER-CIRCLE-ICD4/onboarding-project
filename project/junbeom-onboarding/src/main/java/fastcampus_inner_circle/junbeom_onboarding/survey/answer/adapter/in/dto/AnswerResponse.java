package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long answerId;
    private Long formId;
    private String formName;
    private List<AnswerDetail> answers;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDetail {
        private Long contentId;
        private String contentName;
        private String contentDescribe;
        private String type;
        private String value;
        private List<AnswerOption> options;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerOption {
        private Long optionId;
        private String optionName;
    }
} 