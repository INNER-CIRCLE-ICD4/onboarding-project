package com.innercircle.survey.common.constants;

/**
 * 설문조사 서비스 관련 상수 정의
 * 인스턴스화 하는 것을 막기 위해 생성자에 예외처리
 * private 으로 하여 외부에서 new를 통해 생성하지 못하도록 막는다.
 */
public final class SurveyConstants {

    private SurveyConstants() {
        throw new AssertionError("인스턴스화할 수 없습니다.");
    }

    /**
     * 설문조사 관련 제약사항
     * [설문 받을 항목]은 1개 ~ 10개까지 포함 할 수 있습니다.
     */
    public static final class Survey {
        public static final int MIN_TITLE_LENGTH = 1;
        public static final int MAX_TITLE_LENGTH = 100;
        public static final int MAX_DESCRIPTION_LENGTH = 1000;
        public static final int MIN_QUESTIONS_COUNT = 1;
        public static final int MAX_QUESTIONS_COUNT = 10;
        
        private Survey() {}
    }

    /**
     * 설문 항목 관련 제약사항
     */
    public static final class Question {
        public static final int MIN_TITLE_LENGTH = 1;
        public static final int MAX_TITLE_LENGTH = 200;
        public static final int MAX_DESCRIPTION_LENGTH = 500;
        public static final int MAX_OPTIONS_COUNT = 20;
        public static final int MIN_OPTION_LENGTH = 1;
        public static final int MAX_OPTION_LENGTH = 100;
        
        private Question() {}
    }

    /**
     * 응답 관련 제약사항
     */
    public static final class Response {
        public static final int MAX_TEXT_ANSWER_LENGTH = 2000;
        public static final int MAX_LONG_TEXT_ANSWER_LENGTH = 10000;
        
        private Response() {}
    }

    /**
     * 에러 메시지
     */
    public static final class ErrorMessages {
        public static final String SURVEY_NOT_FOUND = "설문조사를 찾을 수 없습니다.";
        public static final String QUESTION_NOT_FOUND = "설문 항목을 찾을 수 없습니다.";
        public static final String INVALID_QUESTION_COUNT = "설문 항목은 " + Survey.MIN_QUESTIONS_COUNT + "개 이상 " + Survey.MAX_QUESTIONS_COUNT + "개 이하여야 합니다.";
        public static final String REQUIRED_QUESTION_NOT_ANSWERED = "필수 항목에 응답해주세요.";
        public static final String INVALID_CHOICE_ANSWER = "선택 항목에 올바르지 않은 값이 포함되어 있습니다.";
        public static final String MULTIPLE_ANSWERS_FOR_SINGLE_CHOICE = "단일 선택 항목에는 하나의 값만 선택할 수 있습니다.";
        
        private ErrorMessages() {}
    }
}
