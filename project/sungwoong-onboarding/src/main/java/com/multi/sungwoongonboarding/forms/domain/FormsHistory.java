package com.multi.sungwoongonboarding.forms.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

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
}
