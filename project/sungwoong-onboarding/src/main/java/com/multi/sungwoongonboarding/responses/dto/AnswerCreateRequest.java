package com.multi.sungwoongonboarding.responses.dto;

import com.multi.sungwoongonboarding.responses.domain.Answers;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerCreateRequest {

    @NotNull(message = "질문 ID는 필수 입력 항목입니다.")
    private Long questionId;

    // 선택형 질문(단일 선택, 복수 선택)의 경우 필요
    private Long optionId;

    // 텍스트형 질문(단문, 장문)의 경우 필요
    private String answerText;


    @Builder
    public AnswerCreateRequest(Long questionId, Long optionId, String answerText) {
        this.questionId = questionId;
        this.optionId = optionId;
        this.answerText = answerText;
    }

    public Answers toDomain() {
        return Answers.builder()
                .questionId(this.questionId)
                .optionId(this.optionId)
                .answerText(this.answerText)
                .build();
    }
}