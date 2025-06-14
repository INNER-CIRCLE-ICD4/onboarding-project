package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Answer;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.AnswerChoice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record AnswerDto(
        UUID id,
        UUID questionId,
        String textValue,
        List<UUID> selectedChoiceIds
) {
    public static AnswerDto from(Answer answer) {
        List<UUID> choiceIds = answer.getChoices().stream()
                .map(ac -> ac.getChoice().getId())
                .collect(Collectors.toList());

        return new AnswerDto(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getTextValue(),
                choiceIds
        );
    }
}
