package com.wlghsp.querybox.domain.survey

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OptionTest {

    @DisplayName("옵션 값이 정상적으로 설정되면 객체가 생성 성공")
    @Test
    fun optionCreateTest() {
        val value = "서울"

        val option = Option(value)

        assertThat(option.text).isEqualTo(value)
    }

    @DisplayName("옵션 값이 비어 있으면 예외 발생")
    @Test
    fun optionCreateExceptionTest() {
        assertThatThrownBy { Option("") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("옵션 값은 비어 있을 수 없습니다.")
    }
}
