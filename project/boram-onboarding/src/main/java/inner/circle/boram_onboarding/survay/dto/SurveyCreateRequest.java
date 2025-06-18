package inner.circle.boram_onboarding.survay.dto;

import inner.circle.boram_onboarding.survay.enumerated.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SurveyCreateRequest {
    private String title;
    private String description;
    private List<QuestionRequest> questions;

    // QuestionRequest 내부 클래스
    @Getter @Setter
    public static class QuestionRequest {
        private String name;
        private String description;
        private QuestionType type;
        private Boolean required;
        private List<String> options; // 단일/다중 선택일 때만


    }
}
