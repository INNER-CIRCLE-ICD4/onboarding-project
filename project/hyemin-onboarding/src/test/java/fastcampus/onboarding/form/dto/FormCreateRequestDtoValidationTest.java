package fastcampus.onboarding.form.dto;

import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.dto.request.ItemRequestDto;
import fastcampus.onboarding.form.dto.request.OptionRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static fastcampus.onboarding.form.entity.ItemType.SINGLE_CHOICE;
import static org.junit.jupiter.api.Assertions.*;

public class FormCreateRequestDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //모든 필드가 유효한 값을 갖는지 check
    @Test
    void validFormCreateRequestDto_shouldPassValidation() {
        FormCreateRequestDto dto = FormCreateRequestDto.builder()
                .formTitle("Valid Title")
                .formContent("Valid content")
                .items(List.of(
                        ItemRequestDto.builder()
                                .itemTitle("Item 1")
                                .itemContent("Item content")
                                .itemType(SINGLE_CHOICE)
                                .isRequired(true)
                                .options(List.of(
                                        OptionRequestDto.builder()
                                                .optionContent("Option 1")
                                                .build()
                                ))
                                .build()
                ))
                .build();

        Set<ConstraintViolation<FormCreateRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    //NotBlank 검증
    @Test
    void emptyFormTitle_shouldFailValidation() {
        FormCreateRequestDto dto = FormCreateRequestDto.builder()
                .formTitle("  ") // invalid
                .formContent("Valid content")
                .items(List.of())
                .build();

        Set<ConstraintViolation<FormCreateRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Form title is blank, should fail");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("formTitle")));
    }

    //설문 문항 NotEmpty 검증
    @Test
    void nullItems_shouldFailValidation() {
        FormCreateRequestDto dto = FormCreateRequestDto.builder()
                .formTitle("Form title")
                .formContent("Content")
                .items(null) // invalid if @NotNull or @NotEmpty on items
                .build();

        Set<ConstraintViolation<FormCreateRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Items is null, should fail");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("items")));
    }

    //문항의 제목 NotBlank check
    @Test
    void nestedInvalidItem_shouldCascadeAndFail() {
        FormCreateRequestDto dto = FormCreateRequestDto.builder()
                .formTitle("Form title")
                .formContent("Content")
                .items(List.of(
                        ItemRequestDto.builder()
                                .itemTitle("") // invalid
                                .itemContent("Content")
                                .itemType(SINGLE_CHOICE)
                                .isRequired(true)
                                .options(List.of())
                                .build()
                ))
                .build();

        Set<ConstraintViolation<FormCreateRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Item title is blank, should fail");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("items[0].itemTitle")));
    }
}

