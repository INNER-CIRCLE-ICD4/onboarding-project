package com.innercircle.survey.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuestionType 열거형 테스트")
class QuestionTypeTest {

    @Test
    @DisplayName("모든 QuestionType이 정의되어 있다")
    void shouldHaveAllQuestionTypes() {
        // given & when
        QuestionType[] types = QuestionType.values();

        // then
        assertThat(types).hasSize(4);
        assertThat(types).containsExactly(
            QuestionType.SHORT_TEXT,
            QuestionType.LONG_TEXT,
            QuestionType.SINGLE_CHOICE,
            QuestionType.MULTIPLE_CHOICE
        );
    }

    @Test
    @DisplayName("SHORT_TEXT 타입의 속성이 올바르다")
    void shortTextTypeShouldHaveCorrectProperties() {
        // given
        QuestionType type = QuestionType.SHORT_TEXT;

        // when & then
        assertThat(type.getDisplayName()).isEqualTo("단답형");
        assertThat(type.getDescription()).isEqualTo("한 줄 텍스트를 입력받습니다.");
        assertThat(type.isRequiresOptions()).isFalse();
        assertThat(type.isChoiceType()).isFalse();
        assertThat(type.isTextType()).isTrue();
        assertThat(type.isSingleChoice()).isFalse();
        assertThat(type.isMultipleChoice()).isFalse();
    }

    @Test
    @DisplayName("LONG_TEXT 타입의 속성이 올바르다")
    void longTextTypeShouldHaveCorrectProperties() {
        // given
        QuestionType type = QuestionType.LONG_TEXT;

        // when & then
        assertThat(type.getDisplayName()).isEqualTo("장문형");
        assertThat(type.getDescription()).isEqualTo("여러 줄 텍스트를 입력받습니다.");
        assertThat(type.isRequiresOptions()).isFalse();
        assertThat(type.isChoiceType()).isFalse();
        assertThat(type.isTextType()).isTrue();
        assertThat(type.isSingleChoice()).isFalse();
        assertThat(type.isMultipleChoice()).isFalse();
    }

    @Test
    @DisplayName("SINGLE_CHOICE 타입의 속성이 올바르다")
    void singleChoiceTypeShouldHaveCorrectProperties() {
        // given
        QuestionType type = QuestionType.SINGLE_CHOICE;

        // when & then
        assertThat(type.getDisplayName()).isEqualTo("단일 선택 리스트");
        assertThat(type.getDescription()).isEqualTo("여러 옵션 중 하나만 선택합니다.");
        assertThat(type.isRequiresOptions()).isTrue();
        assertThat(type.isChoiceType()).isTrue();
        assertThat(type.isTextType()).isFalse();
        assertThat(type.isSingleChoice()).isTrue();
        assertThat(type.isMultipleChoice()).isFalse();
    }

    @Test
    @DisplayName("MULTIPLE_CHOICE 타입의 속성이 올바르다")
    void multipleChoiceTypeShouldHaveCorrectProperties() {
        // given
        QuestionType type = QuestionType.MULTIPLE_CHOICE;

        // when & then
        assertThat(type.getDisplayName()).isEqualTo("다중 선택 리스트");
        assertThat(type.getDescription()).isEqualTo("여러 옵션 중 다수를 선택합니다.");
        assertThat(type.isRequiresOptions()).isTrue();
        assertThat(type.isChoiceType()).isTrue();
        assertThat(type.isTextType()).isFalse();
        assertThat(type.isSingleChoice()).isFalse();
        assertThat(type.isMultipleChoice()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"SHORT_TEXT", "LONG_TEXT", "SINGLE_CHOICE", "MULTIPLE_CHOICE"})
    @DisplayName("문자열로 QuestionType을 조회할 수 있다")
    void shouldFindQuestionTypeByName(String typeName) {
        // when & then
        assertThat(QuestionType.valueOf(typeName)).isNotNull();
    }

    @Test
    @DisplayName("텍스트 타입들이 올바르게 식별된다")
    void shouldIdentifyTextTypes() {
        // when & then
        assertThat(QuestionType.SHORT_TEXT.isTextType()).isTrue();
        assertThat(QuestionType.LONG_TEXT.isTextType()).isTrue();
        assertThat(QuestionType.SINGLE_CHOICE.isTextType()).isFalse();
        assertThat(QuestionType.MULTIPLE_CHOICE.isTextType()).isFalse();
    }

    @Test
    @DisplayName("선택 타입들이 올바르게 식별된다")
    void shouldIdentifyChoiceTypes() {
        // when & then
        assertThat(QuestionType.SHORT_TEXT.isChoiceType()).isFalse();
        assertThat(QuestionType.LONG_TEXT.isChoiceType()).isFalse();
        assertThat(QuestionType.SINGLE_CHOICE.isChoiceType()).isTrue();
        assertThat(QuestionType.MULTIPLE_CHOICE.isChoiceType()).isTrue();
    }
}
