package com.multi.sungwoongonboarding.submission.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class Answers {

    private final Long id;
    // 응답 ID
    private final Long submissionId;
    // 응답에 대한 질문 ID, 옵션 ID, 답변 내용
    private final Long questionId;
    // 선택 했던 옵션 목록
    private List<SelectedOption> selectedOptions;
    // 답변 내용 (단문, 장문)
    private final String answerText;

}
