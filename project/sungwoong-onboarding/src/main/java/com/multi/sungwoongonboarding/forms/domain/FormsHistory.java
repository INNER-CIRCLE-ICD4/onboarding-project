package com.multi.sungwoongonboarding.forms.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FormsHistory {

    private Long id;
    private Long formId;
    private int version;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private int questionCount;

    public Forms getFormFromHistory() {
        return Forms.builder()
                .id(this.formId)
                .title(this.title)
                .description(this.description)
                .createdAt(this.createdAt)
                .version(this.version)
                .build();
    }
}
