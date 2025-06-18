package com.multi.sungwoongonboarding.submission.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
public class Answers {

    private final Long id;
    // 응답 ID
    private final Long submissionId;
    // 응답에 대한 질문 ID, 옵션 ID, 답변 내용
    private final Long questionId;
    // 선택한 질문 옵션 ID (단일 선택, 복수 선택)
    private final Long optionId;
    // 답변 내용 (단문, 장문)
    private final String answerText;

    @Builder
    public Answers(Long id, Long submissionId, Long questionId, Long optionId, String answerText) {
        this.id = id;
        this.submissionId = submissionId;
        this.questionId = questionId;
        this.optionId = optionId;
        this.answerText = answerText;
    }
}
