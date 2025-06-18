package inner.circle.boram_onboarding.survay.dto;


import inner.circle.boram_onboarding.survay.enumerated.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SurveyUpdateRequest {
    private String title;
    private String description;
    private List<QuestionRequest> questions;

    @Getter @Setter
    public static class QuestionRequest {
        private String name;
        private String description;
        private QuestionType type;
        private Boolean required;
        private List<String> options; // (선택지형일 때만)
        // getters/setters
    }
    // getters/setters
}
