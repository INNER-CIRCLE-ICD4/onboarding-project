package com.multi.sungwoongonboarding.questions.domain;

import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor
public class Questions {
     enum QuestionType {
        //단문, 장문, 단일 선택, 복수 선택
        SHORT_ANSWER, LONG_ANSWER, SINGLE_CHOICE, MULTIPLE_CHOICE
    }

    private final Long id;
    private final String questionText;
    private final QuestionType questionType;
    private final int order;
    private final boolean isRequired;
    private final List<Options> options;

    public static Questions from(QuestionCreateRequest questionCreateRequest) {
        return Questions.builder()
                .questionText(questionCreateRequest.getQuestionText())
                .questionType(QuestionType.valueOf(questionCreateRequest.getQuestionType().toUpperCase()))
                .order(questionCreateRequest.getOrder())
                .isRequired(questionCreateRequest.isRequired())
                .options(
                        questionCreateRequest.getOptionCreateRequests().stream().map(Options::from).collect(Collectors.toList())
                )
                .build();
    }
}
