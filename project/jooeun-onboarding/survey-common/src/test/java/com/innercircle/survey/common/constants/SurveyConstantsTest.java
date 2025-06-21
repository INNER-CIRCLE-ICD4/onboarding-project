package com.innercircle.survey.common.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SurveyConstants 상수 클래스 테스트")
class SurveyConstantsTest {

    @Test
    @DisplayName("SurveyConstants 클래스는 인스턴스화할 수 없다")
    void surveyConstantsShouldNotBeInstantiable() {
        // when & then
        assertThatThrownBy(() -> {
            var constructor = SurveyConstants.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).getCause().isInstanceOf(AssertionError.class)
          .hasMessage("인스턴스화할 수 없습니다.");
    }

    @Test
    @DisplayName("Survey 상수들이 올바른 값을 가진다")
    void surveyShouldHaveCorrectConstants() {
        // when & then
        assertThat(SurveyConstants.Survey.MIN_TITLE_LENGTH).isEqualTo(1);
        assertThat(SurveyConstants.Survey.MAX_TITLE_LENGTH).isEqualTo(100);
        assertThat(SurveyConstants.Survey.MAX_DESCRIPTION_LENGTH).isEqualTo(1000);
        assertThat(SurveyConstants.Survey.MIN_QUESTIONS_COUNT).isEqualTo(1);
        assertThat(SurveyConstants.Survey.MAX_QUESTIONS_COUNT).isEqualTo(10);
    }

    @Test
    @DisplayName("Survey 내부 클래스는 올바르게 정의되어 있다")
    void surveyShouldBeCorrectlyDefined() {
        // when & then
        assertThat(Modifier.isStatic(SurveyConstants.Survey.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(SurveyConstants.Survey.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(SurveyConstants.Survey.class.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("Question 상수들이 올바른 값을 가진다")
    void questionShouldHaveCorrectConstants() {
        // when & then
        assertThat(SurveyConstants.Question.MIN_TITLE_LENGTH).isEqualTo(1);
        assertThat(SurveyConstants.Question.MAX_TITLE_LENGTH).isEqualTo(200);
        assertThat(SurveyConstants.Question.MAX_DESCRIPTION_LENGTH).isEqualTo(500);
        assertThat(SurveyConstants.Question.MAX_OPTIONS_COUNT).isEqualTo(20);
        assertThat(SurveyConstants.Question.MIN_OPTION_LENGTH).isEqualTo(1);
        assertThat(SurveyConstants.Question.MAX_OPTION_LENGTH).isEqualTo(100);
    }

    @Test
    @DisplayName("Question 내부 클래스는 올바르게 정의되어 있다")
    void questionShouldBeCorrectlyDefined() {
        // when & then
        assertThat(Modifier.isStatic(SurveyConstants.Question.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(SurveyConstants.Question.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(SurveyConstants.Question.class.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("Response 상수들이 올바른 값을 가진다")
    void responseShouldHaveCorrectConstants() {
        // when & then
        assertThat(SurveyConstants.Response.MAX_TEXT_ANSWER_LENGTH).isEqualTo(2000);
        assertThat(SurveyConstants.Response.MAX_LONG_TEXT_ANSWER_LENGTH).isEqualTo(10000);
    }

    @Test
    @DisplayName("Response 내부 클래스는 올바르게 정의되어 있다")
    void responseShouldBeCorrectlyDefined() {
        // when & then
        assertThat(Modifier.isStatic(SurveyConstants.Response.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(SurveyConstants.Response.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(SurveyConstants.Response.class.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("ErrorMessages 상수들이 올바른 값을 가진다")
    void errorMessagesShouldHaveCorrectConstants() {
        // when & then
        assertThat(SurveyConstants.ErrorMessages.SURVEY_NOT_FOUND).isEqualTo("설문조사를 찾을 수 없습니다.");
        assertThat(SurveyConstants.ErrorMessages.QUESTION_NOT_FOUND).isEqualTo("설문 항목을 찾을 수 없습니다.");
        assertThat(SurveyConstants.ErrorMessages.INVALID_QUESTION_COUNT)
            .isEqualTo("설문 항목은 1개 이상 10개 이하여야 합니다.");
        assertThat(SurveyConstants.ErrorMessages.REQUIRED_QUESTION_NOT_ANSWERED)
            .isEqualTo("필수 항목에 응답해주세요.");
        assertThat(SurveyConstants.ErrorMessages.INVALID_CHOICE_ANSWER)
            .isEqualTo("선택 항목에 올바르지 않은 값이 포함되어 있습니다.");
        assertThat(SurveyConstants.ErrorMessages.MULTIPLE_ANSWERS_FOR_SINGLE_CHOICE)
            .isEqualTo("단일 선택 항목에는 하나의 값만 선택할 수 있습니다.");
    }

    @Test
    @DisplayName("ErrorMessages 내부 클래스는 올바르게 정의되어 있다")
    void errorMessagesShouldBeCorrectlyDefined() {
        // when & then
        assertThat(Modifier.isStatic(SurveyConstants.ErrorMessages.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(SurveyConstants.ErrorMessages.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(SurveyConstants.ErrorMessages.class.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("상수들의 논리적 관계가 올바르다")
    void constantsShouldHaveLogicalRelationships() {
        // given & when & then
        // Survey 제목 길이 관계
        assertThat(SurveyConstants.Survey.MIN_TITLE_LENGTH)
            .isLessThan(SurveyConstants.Survey.MAX_TITLE_LENGTH);
        
        // Question 제목 길이 관계  
        assertThat(SurveyConstants.Question.MIN_TITLE_LENGTH)
            .isLessThan(SurveyConstants.Question.MAX_TITLE_LENGTH);
        
        // Question 옵션 길이 관계
        assertThat(SurveyConstants.Question.MIN_OPTION_LENGTH)
            .isLessThan(SurveyConstants.Question.MAX_OPTION_LENGTH);
        
        // 질문 수 관계
        assertThat(SurveyConstants.Survey.MIN_QUESTIONS_COUNT)
            .isLessThan(SurveyConstants.Survey.MAX_QUESTIONS_COUNT);
        
        // 응답 길이 관계
        assertThat(SurveyConstants.Response.MAX_TEXT_ANSWER_LENGTH)
            .isLessThan(SurveyConstants.Response.MAX_LONG_TEXT_ANSWER_LENGTH);
        
        // Survey 제목이 Question 제목보다 짧음
        assertThat(SurveyConstants.Survey.MAX_TITLE_LENGTH)
            .isLessThan(SurveyConstants.Question.MAX_TITLE_LENGTH);
        
        // Question 설명이 Survey 설명보다 짧음
        assertThat(SurveyConstants.Question.MAX_DESCRIPTION_LENGTH)
            .isLessThan(SurveyConstants.Survey.MAX_DESCRIPTION_LENGTH);
    }
}
