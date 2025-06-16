package com.wlghsp.querybox.domain.survey

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OptionTest {

    @DisplayName("옵션 값이 정상적으로 설정되면 객체가 생성된다")
    @Test
    fun optionCreateTest() {
        // given
        val value = "서울"

        // when
        val option = Option(value)

        // then
        assertThat(option.value).isEqualTo(value)
    }

    @DisplayName("옵션 값이 비어 있으며 예외가 발생한다.")
    @Test
    fun optionCreateExceptionTest() {
        // given
        val exception = assertThrows<IllegalArgumentException> {
            Option("")
        }

        // when & then
        assertThat(exception.message).isEqualTo("옵션 값은 비어 있을 수 없습니다.")

    }
}