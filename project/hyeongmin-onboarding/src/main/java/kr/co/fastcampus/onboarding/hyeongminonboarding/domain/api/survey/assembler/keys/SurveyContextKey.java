package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.ContextKey;

import java.util.List;

public class SurveyContextKey {


    public static final ContextKey<Survey> SURVEY_CONTEXT_KEY = new ContextKey<Survey>("survey", Survey.class);
    public static final ContextKey<List<SurveyResponse>> SURVEY_RESPONSE_LIST_CONTEXT_KEY = new ContextKey<List<SurveyResponse>>("surveyResponseList", List.class);
    public static final ContextKey<List<Answer>> ANSWER_LIST_CONTEXT_KEY = new ContextKey<List<Answer>>("answerList", List.class);

    public static final ContextKey<List<Question>> QUESTION_LIST_CONTEXT_KEY =
            new ContextKey<>("questionList", List.class); // List<Question>는 선언 쪽에서만 유지됨

    public static final ContextKey<List<QuestionOption>> QUESTION_OPTION_LIST_CONTEXT_KEY =
            new ContextKey<>("questionOptionList", List.class);
}
