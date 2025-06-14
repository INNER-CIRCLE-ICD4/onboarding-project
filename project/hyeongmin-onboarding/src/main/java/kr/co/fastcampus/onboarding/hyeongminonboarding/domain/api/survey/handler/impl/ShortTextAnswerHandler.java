package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.handler.impl;

import jakarta.transaction.NotSupportedException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.handler.AnswerHandler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Answer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ShortTextAnswerHandler implements AnswerHandler {
    @Override
    public boolean supports(QuestionType type) {
        return type == QuestionType.SHORT_TEXT;
    }

    @Override
    public void validate(SubmitAnswerRequest answer, Question question, List<QuestionOption> options) throws NotSupportedException {
        throw new NotSupportedException();
    }

    @Override
    public void validate(SubmitAnswerRequest answer, Question question) {
        if (question.isRequired() && (answer.getAnswerText() == null || answer.getAnswerText().isBlank())) {
            throw new IllegalArgumentException("필수 질문에 응답하지 않았습니다: " + question.getTitle());
        }

        if (answer.getAnswerText() != null && answer.getAnswerText().length() > 255) {
            throw new IllegalArgumentException("답변이 너무 깁니다(최대 255자): " + question.getTitle());
        }
    }


    @Override
    public Answer createEntity(SubmitAnswerRequest answer, Question question, String questionSnapshot, String optionSnapshot) {
        return Answer.builder()
                .question(question)
                .answerText(answer.getAnswerText())
                .questionSnapshotJson(questionSnapshot)
                .optionSnapshotJson(optionSnapshot)
                .build();
    }
}
