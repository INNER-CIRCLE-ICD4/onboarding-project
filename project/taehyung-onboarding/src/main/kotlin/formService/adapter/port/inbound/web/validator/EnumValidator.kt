package formService.adapter.port.inbound.web.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<IsEnum, String> {
    private lateinit var values: List<String>
    private lateinit var messageTemplate: String

    override fun initialize(constraintAnnotation: IsEnum) {
        values =
            constraintAnnotation.enumClass.java.enumConstants
                .map { it.name }

        messageTemplate = constraintAnnotation.message
    }

    override fun isValid(
        value: String,
        context: ConstraintValidatorContext,
    ): Boolean {
        val isValid = values.contains(value)

        if (!isValid) {
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate(
                    "$messageTemplate [${values.joinToString(", ")}]",
                ).addConstraintViolation()
        }

        return isValid
    }
}
