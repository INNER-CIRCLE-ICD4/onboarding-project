package com.multi.sungwoongonboarding.forms.domain;

import com.multi.sungwoongonboarding.questions.domain.Questions;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
public class Forms {
    private Long id;
    private String title;
    private String description;
    private int version;
    private List<Questions> questions;
    private List<FormsHistory> formsHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;

    @Builder
    public Forms(Long id, String title, String description, int version, List<Questions> questions, List<FormsHistory> formsHistories,  LocalDateTime createdAt, LocalDateTime updatedAt, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.version = version;
        this.questions = questions;
        this.formsHistory = formsHistories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public void versionUp() {
        this.version++;
    }

    public Forms findFormVersion(int version) {
        if (version < 1  || this.formsHistory == null || this.formsHistory.isEmpty()) {
            return this;
        }

        Optional<FormsHistory> findVersion = this.formsHistory.stream().filter(history -> version == history.getVersion())
                .findFirst();

        // 이력은 있지만 맞는 버전이 없을 시 null을 반환한다.
        return findVersion.map(FormsHistory::getFormFromHistory).orElse(null);
    }
}
