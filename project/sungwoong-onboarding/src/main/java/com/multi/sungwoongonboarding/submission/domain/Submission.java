package com.multi.sungwoongonboarding.submission.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Submission {

    private Long id;
    private Long formId;
    private String formTitle;
    private String formDescription;
    private int formVersion;
    private LocalDateTime createdAt;
    private List<Answers> answers;
    private String userId;

    public List<Answers> getAnswersByQuestionId(Long questionId) {
        return this.answers.stream().filter(answer -> answer.getQuestionId().equals(questionId)).toList();
    }
}
