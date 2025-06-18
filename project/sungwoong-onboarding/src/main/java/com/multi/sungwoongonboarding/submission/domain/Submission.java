package com.multi.sungwoongonboarding.submission.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    public void setForms(String formTitle, String formDescription) {
        this.formTitle = formTitle;
        this.formDescription = formDescription;
    }
}
