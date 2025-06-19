package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class AnswerCreateRequest {

    private Long questionId;

    // 선택형 질문(단일 선택, 복수 선택)의 경우 필요
    private List<Long> optionIds;

    // 텍스트형 질문(단문, 장문)의 경우 필요
    private String answerText;

    public Answers toDomainForSave(List<SelectedOption> selectedOptions) {
        return Answers.builder()
                .questionId(this.questionId)
                .selectedOptions(selectedOptions)
                .answerText(this.answerText)
                .build();
    }

    /**
     * 중복 옵션 검사
     */
    public boolean hasDuplicateOptions() {
        if (optionIds == null) return false;
        return optionIds.size() != new HashSet<>(optionIds).size();
    }

    /**
     * 중복된 옵션들 반환
     */
    public Set<Long> getDuplicateOptionIds() {
        if (optionIds == null) return Set.of();

        Set<Long> seen = new HashSet<>();
        Set<Long> duplicates = new HashSet<>();

        for (Long optionId : optionIds) {
            if (!seen.add(optionId)) {
                duplicates.add(optionId);
            }
        }
        return duplicates;
    }


    /**
     * 중복 제거된 고유 옵션 ID들 반환
     */
    public Set<Long> getUniqueOptionIds() {
        if (optionIds == null) return Set.of();
        return new LinkedHashSet<>(optionIds); // 순서 보존하면서 중복 제거
    }

}