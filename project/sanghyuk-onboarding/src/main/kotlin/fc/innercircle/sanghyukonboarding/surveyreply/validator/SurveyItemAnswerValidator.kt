package fc.innercircle.sanghyukonboarding.surveyreply.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.survey.domain.model.vo.InputType

object SurveyItemAnswerValidator {
    /**
     * [설문 항목 답변 입력값 유효성 검증]
     *
     * 설문 항목 답변 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
     * - question: 빈 문자열이 아니어야 하며, 최대 500자 이내
     * - description: 빈 문자열이 아니어야 하며, 최대 1000자 이내
     * - surveyInputType: 빈 문자열이 아니어야 하며, [InputType]의 항목들 중 하나여야 합니다.
     * - answer: 입력값이 있으면, 빈 문자열이 아니어야 하며, 최대 50000자 이내
     */
    fun validateQuestion(question: String) {
        if (question.isBlank() || question.length > 500) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(question))
        }
    }

    fun validateDescription(description: String) {
        if (description.isBlank() || description.length > 1000) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_ANSWER_DESCRIPTION.withArgs(description))
        }
    }

    fun validateSurveyInputType(surveyInputType: String) {
        if (surveyInputType.isBlank() || !InputType.isValidType(surveyInputType)) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_ANSWER_SURVEY_INPUT_TYPE.withArgs(surveyInputType))
        }
    }

    fun validateAnswer(answer: String) {
        if (answer.isNotBlank() && answer.length > 50000) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(answer))
        }
    }
}
