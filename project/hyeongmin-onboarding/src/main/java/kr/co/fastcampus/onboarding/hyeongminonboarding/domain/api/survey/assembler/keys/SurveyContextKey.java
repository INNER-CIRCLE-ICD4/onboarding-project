package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.ContextKey;

import java.util.List;

public class SurveyContextKey {


    public static final ContextKey<Survey> SURVEY_CONTEXT_KEY = new ContextKey<Survey>("survey", Survey.class);

    public static final ContextKey<List<Question>> QUESTION_LIST_CONTEXT_KEY =
            new ContextKey<>("questionList", List.class); // List<Question>는 선언 쪽에서만 유지됨

    public static final ContextKey<List<QuestionOption>> QUESTION_OPTION_CONTEXT_KEY =
            new ContextKey<>("questionOptionList", List.class);
}
