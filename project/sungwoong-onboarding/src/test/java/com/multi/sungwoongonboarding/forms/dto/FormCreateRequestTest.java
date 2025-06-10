package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
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
    @DisplayName("Enum validation 테스트 - 질문 유형이 잘못 넘어온 경우")
    public void testQuestionCreateRequestValidation() {
        // Given
        QuestionCreateRequest questionCreateRequest_fail = new QuestionCreateRequest("question1", "옳지 않은 유형", 1, true, null);
        QuestionCreateRequest questionCreateRequest_success = new QuestionCreateRequest("question1", "SHORT_ANSWER", 1, true, null);

        // When
        var validate_fail = validator.validate(questionCreateRequest_fail);
        var validate_success = validator.validate(questionCreateRequest_success);

        // Then
        Assertions.assertThat(validate_fail.size()).isEqualTo(1);
        Assertions.assertThat(validate_success.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("OptionCheck 검증 어노테이션 테스트 - 선택 형식의 질문에 옵션이 없는 경우")
    public void testOptionCreateRequestValidation() {
        // Given
        QuestionCreateRequest multiple_or_single_option_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, null);
        QuestionCreateRequest multiple_or_single_option_empty = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of());
        QuestionCreateRequest multiple_or_single_not_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of(new OptionCreateRequest("option1", 1)));
        QuestionCreateRequest short_or_long_not_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of(new OptionCreateRequest("option1", 1)));

        // When
        var validateNull = validator.validate(multiple_or_single_option_null);
        var validateEmpty = validator.validate(multiple_or_single_option_empty);
        var validateNotNull1 = validator.validate(multiple_or_single_not_null);
        var validateNotNull2 = validator.validate(short_or_long_not_null);

        // Then
        Assertions.assertThat(validateNull.size()).isEqualTo(1);
        Assertions.assertThat(validateEmpty.size()).isEqualTo(1);
        Assertions.assertThat(validateNotNull1.size()).isEqualTo(0);
        Assertions.assertThat(validateNotNull2.size()).isEqualTo(0);
    }





}