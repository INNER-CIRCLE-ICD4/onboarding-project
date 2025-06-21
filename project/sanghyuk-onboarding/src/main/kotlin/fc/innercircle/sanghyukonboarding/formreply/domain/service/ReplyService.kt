package fc.innercircle.sanghyukonboarding.formreply.domain.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.common.domain.service.port.ClockHolder
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.Question
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.domain.service.dto.param.AnswerParam
import org.springframework.stereotype.Service

@Service
class ReplyService(private val clockHolder: ClockHolder) {

    /**
     * 사용자의 설문 답변을 처리하여 FormReply 객체를 생성합니다.
     */
    fun reply(form: Form, answerParams: List<AnswerParam>): FormReply {
        val answers = createAnswersFromParams(form, answerParams)
        return FormReply(
            formId = form.id,
            answers = answers,
            submittedAt = clockHolder.localDateTime()
        )
    }

    /**
     * 전달된 답변 파라미터 리스트를 Answer 객체 리스트로 변환합니다.
     * 이 과정에서 각 답변에 대한 유효성 검사를 수행합니다.
     */
    private fun createAnswersFromParams(form: Form, params: List<AnswerParam>): List<Answer> {
        return params.map { param ->
            val question = form.questions.getById(param.questionId)
            validateAnswer(question, param)
            val answer = Answer(
                values = param.values,
                questionId = question.id
            )
            validateRequiredAnswer(question, answer)
            answer
        }
    }

    private fun validateRequiredAnswer(
        question: Question,
        answer: Answer,
    ) {
        if (question.required && answer.isEmpty()) {
            throw CustomException(ErrorCode.REQUIRED_QUESTION_ANSWER.withArgs(question.title))
        }
    }

    /**
     * 개별 답변에 대한 모든 유효성 검사를 수행하는 진입점입니다.
     */
    private fun validateAnswer(question: Question, param: AnswerParam) {
        validateAnswerByType(question, param)
    }

    /**
     * 정책: 선택형 질문의 경우, 제출된 옵션 값은 반드시 질문에 정의된 옵션이어야 합니다.
     */
    private fun validateSelectedOptions(question: Question, param: AnswerParam) {
        if (param.values.isEmpty()) return

        val validOptions = question.options.toSet()
        val invalidOptions = param.values.filterNot { validOptions.contains(it) }

        if (invalidOptions.isNotEmpty()) {
            throw CustomException(
                ErrorCode.INVALID_SELECTABLE_OPTION.withArgs(
                    param.questionId,
                    invalidOptions.joinToString(", ")
                )
            )
        }
    }

    /**
     * 질문 유형에 따른 답변의 유효성을 검증합니다.
     * 1. 답변은 텍스트와 선택지를 동시에 포함할 수 없습니다.
     * 2. 텍스트형 질문의 답변은 비어있을 수 없습니다.
     * 3. 선택형 질문의 답변은 최소 하나 이상의 선택지를 포함해야 합니다.
     * 4. 단일 선택형 질문의 답변은 하나의 선택지만을 포함해야 합니다.
     */
    private fun validateAnswerByType(question: Question, param: AnswerParam) {
        val questionType = question.type
        when {
            questionType.isTextType() -> {
                if (param.values.size > 1) {
                    throw CustomException(ErrorCode.INVALID_TEXT_QUESTION_ANSWER.withArgs(question.title))
                }
            }
            questionType.isSelectableType() -> {
                if (param.values.isEmpty()) {
                    throw CustomException(ErrorCode.INVALID_SELECTABLE_QUESTION_ANSWER.withArgs(question.title))
                }
                if (questionType.isSingleSelectableType() && param.values.size > 1) {
                    throw CustomException(ErrorCode.INVALID_SINGLE_SELECTABLE_QUESTION_ANSWER.withArgs(question.title))
                }
                validateSelectedOptions(question, param)
            }
        }
    }
}
