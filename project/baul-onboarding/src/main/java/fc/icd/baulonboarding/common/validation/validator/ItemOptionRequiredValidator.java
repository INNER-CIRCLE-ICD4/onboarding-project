package fc.icd.baulonboarding.common.validation.validator;

import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.common.validation.annotation.ValidItemOptions;
import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ItemOptionRequiredValidator implements ConstraintValidator<ValidItemOptions, SurveyDto.RegisterSurveyItemRequest> {
    @Override
    public boolean isValid(SurveyDto.RegisterSurveyItemRequest value, ConstraintValidatorContext context) {
        if (value == null) return true;

        InputType inputType = value.getInputType();
        List<SurveyDto.RegisterSurveyItemOptionRequest> options = value.getSurveyItemOptionList();

        if ((inputType == InputType.SINGLE_CHOICE || inputType == InputType.MULTI_CHOICE)) {
            if (options == null || options.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("선택형 항목에는 옵션이 1개 이상 필수입니다.")
                        .addPropertyNode("ItemOptionList")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
