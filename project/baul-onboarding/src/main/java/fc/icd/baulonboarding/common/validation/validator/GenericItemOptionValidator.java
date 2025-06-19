package fc.icd.baulonboarding.common.validation.validator;

import fc.icd.baulonboarding.common.model.code.InputType;
import fc.icd.baulonboarding.common.validation.annotation.ValidItemOptions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.List;

public class GenericItemOptionValidator implements ConstraintValidator<ValidItemOptions, Object> {
    private String inputTypeField;
    private String optionListField;

    @Override
    public void initialize(ValidItemOptions annotation) {
        this.inputTypeField = annotation.inputTypeField();
        this.optionListField = annotation.optionListField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            Class<?> clazz = value.getClass();

            Field inputType = clazz.getDeclaredField(inputTypeField);
            inputType.setAccessible(true);
            Object inputTypeValue = inputType.get(value);

            Field optionList = clazz.getDeclaredField(optionListField);
            optionList.setAccessible(true);
            Object optionListValue = optionList.get(value);

            if (!(inputTypeValue instanceof InputType)) return true;

            InputType type = (InputType) inputTypeValue;

            if (type == InputType.SINGLE_CHOICE || type == InputType.MULTI_CHOICE) {
                if (!(optionListValue instanceof List) || ((List<?>) optionListValue).isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("선택형 항목에는 옵션이 1개 이상 필수입니다.")
                            .addPropertyNode(optionListField)
                            .addConstraintViolation();
                    return false;
                }
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }

        return true;
    }
}
