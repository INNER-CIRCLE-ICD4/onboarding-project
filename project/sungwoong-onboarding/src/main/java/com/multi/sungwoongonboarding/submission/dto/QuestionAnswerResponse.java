package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class QuestionAnswerResponse {

    private final Long questionId;
    private final String questionText;
    private final Questions.QuestionType questionType;
    private final boolean isRequired;
    private final List<SelectedOption> selectedOptions;
    private final String answerText;

    public static QuestionAnswerResponse fromQuestionWithAnswers(Questions question, Answers answers) {

        return QuestionAnswerResponse.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .isRequired(question.isRequired())
                .questionType(question.getQuestionType())
                .selectedOptions(answers.getSelectedOptions())
                .answerText(answers.getAnswerText())
                .build();
    }

}
