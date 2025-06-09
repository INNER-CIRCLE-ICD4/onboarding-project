package com.multi.sungwoongonboarding.questions.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuestionCreateRequestTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("custom validation 테스트 - 질문 유형이 잘못 넘어온 경우")
    public void testQuestionCreateRequestValidation() {
        // Given
        QuestionCreateRequest questionCreateRequest = new QuestionCreateRequest("question1", "단문", 1, true,null);

        // When
        Set<ConstraintViolation<QuestionCreateRequest>> validate = validator.validate(questionCreateRequest);

        // Then
        Assertions.assertThat(validate.size()).isEqualTo(1);

    }

}