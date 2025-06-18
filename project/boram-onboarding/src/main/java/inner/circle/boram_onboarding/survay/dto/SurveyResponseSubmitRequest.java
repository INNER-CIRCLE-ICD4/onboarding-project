package inner.circle.boram_onboarding.survay.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SurveyResponseSubmitRequest {
    private List<AnswerRequest> answers;

    @Getter @Setter
    public static class AnswerRequest {
        private String questionName;   // 설문 당시의 질문명(문항 식별자)
        private Object value;          // 단답형/장문형: String, 선택형: String/List<String>
    }
}
