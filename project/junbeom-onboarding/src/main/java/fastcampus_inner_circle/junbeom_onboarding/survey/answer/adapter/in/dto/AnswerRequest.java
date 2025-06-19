package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class AnswerRequest {
    private Long formId;
    private List<AnswerDetail> answers;

    @Data
    public static class AnswerDetail {
        private Long contentId;
        private String contentName;
        private String contentDescribe;
        private String type;
        private String value; // 주관식일 때만 사용
        private List<AnswerDetailOptions> options; // 선택형일 때만 사용
    }

    @Data
    public static class AnswerDetailOptions {
        private Long optionId;
        private String text;
    }


} 