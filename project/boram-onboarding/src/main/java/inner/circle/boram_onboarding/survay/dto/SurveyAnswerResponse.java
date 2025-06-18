package inner.circle.boram_onboarding.survay.dto;


// 전체 응답 리스트 조회 클래스

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SurveyAnswerResponse {
    private Long responseId;
    private String submittedAt;
    private List<AnswerDto> answers;

    @Getter @Setter
    public static class AnswerDto {
        private String questionName;
        private Object value; // (단답/장문: String, 선택형: List<String>)
    }
}
