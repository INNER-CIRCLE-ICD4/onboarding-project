package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Answer;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ResponseDto(
        UUID id,
        UUID surveyId,
        String surveySnapshot,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<AnswerDto> answers
) {
    public static ResponseDto from(Response response) {
        return new ResponseDto(
                response.getId(),
                response.getSurvey().getId(),
                response.getSurveySnapshot(),
                response.getCreatedAt(),
                response.getUpdatedAt(),
                response.getAnswers().stream().map(AnswerDto::from).collect(Collectors.toList())
        );
    }
}
