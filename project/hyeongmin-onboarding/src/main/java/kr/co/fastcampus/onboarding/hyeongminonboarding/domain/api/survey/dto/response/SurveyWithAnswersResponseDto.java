package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class SurveyWithAnswersResponseDto {
    private Long surveyId;
    private String title;
    private String description;


    private List<SurveyAnswerResponseDto> responses;

    @Data
    @Builder
    public static class SurveyAnswerResponseDto {
        private Long respondentId;
        private LocalDateTime submittedAt;
        private Integer surveyVersion;
        private List<AnswerDetailDto> answers;
    }

    @Data
    @Builder
    public static class AnswerDetailDto {
        private Long questionId;

        private String questionTitle;                // 현재 질문 제목
        private String questionDetail;                // 현재 질문 제목
        private QuestionType questionType;
        private Boolean required;

        private List<Long> selectedOptionIds;        // 선택지 응답인 경우
        private String answerText;                   // 텍스트 응답인 경우

        private QuestionSnapshotDto questionSnapshot;
        private List<QuestionOptionSnapshotDto> optionSnapshot;
    }


    @Data
    @Builder
    public static class QuestionSnapshotDto {
        private String title;
        private QuestionType type;
        private Boolean required;
        private String detail;
    }

    @Data
    @Builder
    public static class QuestionOptionSnapshotDto {
        private Long id;
        private String optionValue;
    }

}
