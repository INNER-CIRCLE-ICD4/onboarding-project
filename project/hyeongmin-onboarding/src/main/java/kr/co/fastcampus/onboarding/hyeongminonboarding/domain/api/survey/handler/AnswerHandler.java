package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.handler;

import jakarta.transaction.NotSupportedException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Answer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;

import java.util.List;

public interface AnswerHandler {
    boolean supports(QuestionType type);
    void validate(SubmitAnswerRequest answer, Question question, List<QuestionOption> options) throws NotSupportedException;
    void validate(SubmitAnswerRequest answer, Question question) throws NotSupportedException;
    Answer createEntity(SubmitAnswerRequest answer, Question question, String questionSnapshot, String optionSnapshot);
}
