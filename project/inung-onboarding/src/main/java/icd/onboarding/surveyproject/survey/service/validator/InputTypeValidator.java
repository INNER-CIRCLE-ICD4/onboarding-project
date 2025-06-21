package icd.onboarding.surveyproject.survey.service.validator;

import icd.onboarding.surveyproject.survey.service.enums.InputType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InputTypeValidator implements ConstraintValidator<ValidInputType, String> {

	@Override
	public boolean isValid (String value, ConstraintValidatorContext context) {
		return contains(value);
	}

	public static boolean contains (String inputType) {
		if (inputType == null) return false;

		try {
			InputType.valueOf(inputType);
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
}
