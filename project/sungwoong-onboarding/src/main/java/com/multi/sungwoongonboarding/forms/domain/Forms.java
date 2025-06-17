package com.multi.sungwoongonboarding.forms.domain;

import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Forms {
    private Long id;
    private String title;
    private String description;
    private String questionType;
    private int version;
    private List<Questions> questions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;

    @Builder
    public Forms(Long id, String title, String description, String questionType, int version, List<Questions> questions, LocalDateTime createdAt, LocalDateTime updatedAt, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questionType = questionType;
        this.version = version;
        this.questions = questions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public void versionUp() {
        this.version++;
    }
}
