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
public class MultipleChoiceAnswerHandler implements AnswerHandler {


    @Override
    public boolean supports(QuestionType type) {
        return type == QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public void validate(SubmitAnswerRequest answer, Question question, List<QuestionOption> options) throws NotSupportedException {
        List<Long> selected = answer.getSelectedOptionIds();

        if (question.isRequired() && (selected == null || selected.isEmpty())) {
            throw new IllegalArgumentException("필수 질문에 응답하지 않았습니다: " + question.getTitle());
        }

        if (selected != null && selected.stream().anyMatch(id -> options.stream().noneMatch(opt -> opt.getId().equals(id)))) {
            throw new IllegalArgumentException("선택한 옵션이 해당 질문의 옵션에 포함되어 있지 않습니다: " + question.getTitle());
        }
    }

    @Override
    public void validate(SubmitAnswerRequest answer, Question question) throws NotSupportedException {
        throw new NotSupportedException();
    }

    @Override
    public Answer createEntity(SubmitAnswerRequest answer, Question question, String questionSnapshot, String optionSnapshot) {
        return Answer.builder()
                .question(question)
                .selectedOptionIds(answer.getSelectedOptionIds())
                .answerText(null)
                .questionSnapshotJson(questionSnapshot)
                .optionSnapshotJson(optionSnapshot)
                .build();
    }
}
