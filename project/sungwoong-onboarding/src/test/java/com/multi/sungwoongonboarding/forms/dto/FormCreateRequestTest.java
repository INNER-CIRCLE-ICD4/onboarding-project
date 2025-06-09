package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


@SpringBootTest
@ActiveProfiles("test")
class FormCreateRequestTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("설문지 요청 객체 유효성 검사")
    public void testFormCreateRequestValidation() {
        // Given
        List<QuestionCreateRequest> questionCreateRequests =
            IntStream.range(1, 12).mapToObj(i ->
                new QuestionCreateRequest(String.format("q_%d", i) + i, Questions.QuestionType.LONG_ANSWER.name(), i, true, null)
            ).toList();

        FormCreateRequest formCreateRequest = new FormCreateRequest("설문 제목", "설문 설명", questionCreateRequests);

        // When
        var violations = validator.validate(formCreateRequest);

        // Then
        Assertions.assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("custom validation 테스트 - 질문 유형이 잘못 넘어온 경우")
    public void testQuestionCreateRequestValidation() {
        // Given
        QuestionCreateRequest questionCreateRequest = new QuestionCreateRequest("question1", "옳지 않은 유형", 1, true,null);

        // When
        Set<ConstraintViolation<QuestionCreateRequest>> validate = validator.validate(questionCreateRequest);

        // Then
        Assertions.assertThat(validate.size()).isEqualTo(1);

    }
}