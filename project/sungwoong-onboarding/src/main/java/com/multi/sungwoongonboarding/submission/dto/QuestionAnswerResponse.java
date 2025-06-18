package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class QuestionAnswerResponse {

    private final Long questionId;
    private final String questionText;
    private final Questions.QuestionType questionType;
    private final boolean isRequired;
    private final Set<String> answerResponses;

    public static QuestionAnswerResponse fromQuestionWithAnswers(Questions question, List<Answers> answers) {

        return QuestionAnswerResponse.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .isRequired(question.isRequired())
                .questionType(question.getQuestionType())
                .answerResponses(answers.stream().map(Answers::getAnswerText).collect(Collectors.toSet()))
                .build();
    }

}
