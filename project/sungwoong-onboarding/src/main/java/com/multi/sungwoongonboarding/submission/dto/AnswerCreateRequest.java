package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.common.valid.AnswerOptionValid;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AnswerOptionValid
public class AnswerCreateRequest {

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