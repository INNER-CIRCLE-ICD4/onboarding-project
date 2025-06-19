package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Answer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record AnswerDto(
        UUID questionId,
        String text,
        List<String> selectedOptions
) {
    public static AnswerDto from(Answer answer) {
        return new AnswerDto(
                answer.getQuestion().getId(),
                answer.getText(),
                answer.getSelectedOptions() == null ? null :
                        answer.getSelectedOptions().stream()
                                .map(choice -> choice.getId().toString())  // ✅ 여기 수정
                                .collect(Collectors.toList())
        );
    }
}
