package com.multi.sungwoongonboarding.forms.domain;

import com.multi.sungwoongonboarding.questions.domain.Questions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class Forms {
    private Long id;
    private String title;
    private String description;
    private int version;
    private List<Questions> questions;
    private List<FormsHistory> formsHistories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;

    public void versionUp() {
        this.version++;
    }

    public Forms findFormVersion(int version) {
        if (version < 1  || this.formsHistories == null || this.formsHistories.isEmpty()) {
            return this;
        }

        Optional<FormsHistory> findVersion = this.formsHistories.stream().filter(history -> version == history.getVersion())
                .findFirst();

        // 이력은 있지만 맞는 버전이 없을 시 null을 반환한다.
        return findVersion.map(FormsHistory::getFormFromHistory).orElse(null);
    }
}
