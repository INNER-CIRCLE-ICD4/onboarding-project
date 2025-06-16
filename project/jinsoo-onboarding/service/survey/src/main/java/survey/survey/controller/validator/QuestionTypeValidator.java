package survey.survey.controller.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import survey.survey.entity.surveyquestion.InputType;
import survey.survey.controller.request.SurveyFormCreateRequest;

class QuestionTypeValidator implements ConstraintValidator<ValidQuestion, SurveyFormCreateRequest.QuestionCreateRequest> {
    @Override
    public boolean isValid(SurveyFormCreateRequest.QuestionCreateRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getInputType() == null) {
            return true;
        }

        InputType inputType = request.getInputType();

        if (inputType.requiresCandidates()) {
            if (request.getCandidates() == null || request.getCandidates().isEmpty()) {
                addConstraintViolation(context,
                        inputType + " 유형 질문에는 최소 하나 이상의 선택지가 필요합니다.");
                return false;
            }
        } else {
            if (request.getCandidates() != null && !request.getCandidates().isEmpty()) {
                addConstraintViolation(context,
                        inputType + " 유형 질문에는 선택지를 추가할 수 없습니다.");
                return false;
            }
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}

