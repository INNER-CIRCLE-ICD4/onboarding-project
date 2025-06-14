package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.handler;


import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerHandlerFactory {
    private final List<AnswerHandler> handlers;

    public AnswerHandler getHandler(QuestionType type) {
        return handlers.stream()
                .filter(h -> h.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 질문 타입"));
    }
}
