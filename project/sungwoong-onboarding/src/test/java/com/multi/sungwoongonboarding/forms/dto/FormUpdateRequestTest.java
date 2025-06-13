package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.questions.dto.QuestionUpdateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class FormUpdateRequestTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("FormUpdateRequest 필드 검증")
    public void 설문지_수정_요청값_검증() {
        // Given
        FormUpdateRequest id_null = FormUpdateRequest.builder().title("Updated Title").description("Updated Description").build();
        FormUpdateRequest title_null = FormUpdateRequest.builder().id(1L).description("Updated Description").build();
        FormUpdateRequest description_null = FormUpdateRequest.builder().id(2L).title("Updated Title").build();

        //When
        var idValid = validator.validate(id_null);
        var titleValid = validator.validate(title_null);
        var descriptionValid = validator.validate(description_null);

        // then
        assertThat(idValid.size()).isEqualTo(1);
        assertThat(titleValid.size()).isEqualTo(2);
        assertThat(descriptionValid.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("QuestionUpdateRequest 필드 검증")
    public void 질문_수정_요청값_검증() {
        // Given
        QuestionUpdateRequest text_null = QuestionUpdateRequest.builder().id(1L).questionType("SINGLE_CHOICE").deleted(false).isRequired(true).build();
        QuestionUpdateRequest bad_type = QuestionUpdateRequest.builder().id(1L).questionText("Updated Question").questionType("잘못된 타입").deleted(false).isRequired(true).build();
        QuestionUpdateRequest type_null = QuestionUpdateRequest.builder().id(1L).questionText("Updated Question").deleted(false).isRequired(true).build();
        QuestionUpdateRequest deleted_null = QuestionUpdateRequest.builder().id(1L).questionText("SINGLE_CHOICE").isRequired(true).build();
        QuestionUpdateRequest required_null = QuestionUpdateRequest.builder().id(1L).questionType("SINGLE_CHOICE").questionText("Updated Question").deleted(false).build();

        // When
        var text_valid = validator.validate(text_null);
        var bad_valid = validator.validate(bad_type);
        var type_valid = validator.validate(type_null);
        var deleted_valid = validator.validate(deleted_null);
        var required_valid = validator.validate(required_null);

        // Then
        assertThat(text_valid.size()).isEqualTo(2);
        //타입 검증 실페 - 타입을 알 수 없으니 옵션이 필요한지 체크할 수 없다.
        assertThat(bad_valid.size()).isEqualTo(1);
        assertThat(type_valid.size()).isEqualTo(1);

        assertThat(deleted_valid.size()).isEqualTo(2);
        assertThat(required_valid.size()).isEqualTo(2);
    }


}