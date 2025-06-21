package com.wlghsp.querybox.domain.survey

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OptionsTest {

    @DisplayName("Options 생성 - 유효한 값일 경우 성공")
    @Test
    fun createOptions_success() {
        val options = Options.of(listOf(Option("A"), Option("B")))

        assertThat(options.options).hasSize(2)
        assertThat(options.options).containsOnly(Option("A"), Option("B"))
    }

    @DisplayName("Options.isEmpty, isNotEmpty 동작 확인")
    @Test
    fun optionsEmptyCheck() {
        val notEmptyOptions = Options.of(listOf(Option("X")))
        assertThat(notEmptyOptions.isEmpty()).isFalse
        assertThat(notEmptyOptions.isNotEmpty()).isTrue
    }

    @DisplayName("Options 생성 - 빈 리스트일 경우 예외 발생")
    @Test
    fun createOptions_empty_shouldFail() {
        assertThatThrownBy { Options.of(emptyList()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("옵션은 비어 있을 수 없습니다.")
    }

    @DisplayName("Options 생성 - 중복 값이 있을 경우 예외 발생")
    @Test
    fun createOptions_withDuplicateValues_shouldFail() {
        assertThatThrownBy {
            Options.of(listOf(Option("중복"), Option("중복")))
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("옵션 값은 중복될 수 없습니다.")
    }
}
