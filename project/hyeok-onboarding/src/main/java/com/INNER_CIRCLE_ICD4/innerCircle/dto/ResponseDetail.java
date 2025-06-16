package com.INNER_CIRCLE_ICD4.innerCircle.dto;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Response;

import java.util.List;
import java.util.UUID;

public record ResponseDetail(
        UUID id,
        List<AnswerDto> answers
) {
    public static ResponseDetail from(Response response) {
        List<AnswerDto> answerDtos = response.getAnswers().stream()
                .map(AnswerDto::from)
                .toList();
        return new ResponseDetail(response.getId(), answerDtos);
    }
}
