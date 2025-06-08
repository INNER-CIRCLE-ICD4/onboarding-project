package com.multi.sungwoongonboarding.forms.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FormServiceTest {

    @Test
    @DisplayName("설문 조사서 등록 테스트")
    public void testCreateForm() {
        // Given
        // 설문 조사서 등록에 필요한 데이터 준비


        // When
        // FormService를 사용하여 설문 조사서 등록

        // Then
        // 등록된 설문 조사서가 올바르게 저장되었는지 검증
    }
}