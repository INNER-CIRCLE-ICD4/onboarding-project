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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class FormCreateRequestTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("FormCreateRequest 요청 객체 유효성 검사 - 질문이 10개가 넘어 검증 실패")
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
        assertThat(violations).hasSize(1);
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
        assertThat(validate_fail.size()).isEqualTo(1);
        assertThat(validate_success.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("OptionCheck 검증 어노테이션 테스트 - 선택 형식의 질문에 옵션이 없는 경우")
    public void testOptionCreateRequestValidation() {
        // Given
        // 질문 유형: Multiple
        QuestionCreateRequest multiple_or_single_option_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, null);
        QuestionCreateRequest multiple_or_single_option_empty = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of());
        QuestionCreateRequest multiple_or_single_not_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of(new OptionCreateRequest("option1", 1)));
        // 질문 유형: Single
        QuestionCreateRequest single_or_single_option_null = new QuestionCreateRequest("question1", Questions.QuestionType.SINGLE_CHOICE.name(), 1, true, null);
        QuestionCreateRequest single_or_single_option_empty = new QuestionCreateRequest("question1", Questions.QuestionType.SINGLE_CHOICE.name(), 1, true, List.of());
        QuestionCreateRequest single_or_single_not_null = new QuestionCreateRequest("question1", Questions.QuestionType.SINGLE_CHOICE.name(), 1, true, List.of(new OptionCreateRequest("option1", 1)));
        // 질문 유형: Short
        QuestionCreateRequest short_or_long_not_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of(new OptionCreateRequest("option1", 1)));

        // When
        // 질문 유형: Multiple
        var multipleValidateNull = validator.validate(multiple_or_single_option_null);
        var multipleValidateEmpty = validator.validate(multiple_or_single_option_empty);
        var multipleValidateNotNull = validator.validate(multiple_or_single_not_null);
        // 질문 유형: Single
        var singleValidateNull = validator.validate(single_or_single_option_null);
        var singleValidateEmpty = validator.validate(single_or_single_option_empty);
        var singleValidateNotNull = validator.validate(single_or_single_not_null);
        // 질문 유형: Short
        var shortValidateNotNull = validator.validate(short_or_long_not_null);

        // Then
        // 질문 유형: Multiple
        assertThat(multipleValidateNull.size()).isEqualTo(1);
        assertThat(multipleValidateEmpty.size()).isEqualTo(1);
        assertThat(multipleValidateNotNull.size()).isEqualTo(0);
        // 질문 유형: Single
        assertThat(singleValidateNull.size()).isEqualTo(1);
        assertThat(singleValidateEmpty.size()).isEqualTo(1);
        assertThat(singleValidateNotNull.size()).isEqualTo(0);
        // 질문 유형: Short
        assertThat(shortValidateNotNull.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("OptionCheck 검증 어노테이션 테스트 - 선택 형식의 질문에 옵션 데이터가 없는 경우")
    public void testOptionCreateRequestValidationWithEmptyOption() {
        // Given
        QuestionCreateRequest questionRequest_optionText_null = new QuestionCreateRequest("question1", Questions.QuestionType.MULTIPLE_CHOICE.name(), 1, true, List.of(new OptionCreateRequest("" , 1)));
        QuestionCreateRequest questionRequests_optionText_null = new QuestionCreateRequest(
                "question1",
                Questions.QuestionType.MULTIPLE_CHOICE.name(),
                1,
                true,
                List.of(new OptionCreateRequest("" , 1),
                        new OptionCreateRequest("" , 2),
                        new OptionCreateRequest("" , 3),
                        new OptionCreateRequest("" , 4),
                        new OptionCreateRequest("" , 5),
                        new OptionCreateRequest("" , 6),
                        new OptionCreateRequest("" , 7),
                        new OptionCreateRequest("" , 8),
                        new OptionCreateRequest("" , 9),
                        new OptionCreateRequest("" , 10)
                )
        );

        // When
        var violations_option_size_1 = validator.validate(questionRequest_optionText_null);
        var violations_option_size_10 = validator.validate(questionRequests_optionText_null);

        // Then
        assertThat(violations_option_size_1).hasSize(1);
        assertThat(violations_option_size_10).hasSize(10);
    }
}