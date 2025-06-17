package icd.onboarding.surveyproject.survey.service.validator;

import icd.onboarding.surveyproject.survey.service.enums.InputType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InputTypeValidator implements ConstraintValidator<ValidInputType, String> {

	@Override
	public boolean isValid (String value, ConstraintValidatorContext context) {
		return contains(value);
	}

	public static boolean contains (String name) {
		if (name == null) return false;

		for (InputType type : InputType.values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
