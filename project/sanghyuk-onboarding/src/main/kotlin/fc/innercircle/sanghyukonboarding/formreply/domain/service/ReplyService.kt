package fc.innercircle.sanghyukonboarding.formreply.domain.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.common.domain.service.port.ClockHolder
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.formreply.domain.service.dto.param.AnswerParam
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import org.springframework.stereotype.Service

@Service
class ReplyService(private val clockHolder: ClockHolder) {

    /**
     * 사용자의 설문 답변을 처리하여 FormReply 객체를 생성합니다.
     */
    fun reply(form: Form, answerParams: List<AnswerParam>): FormReply {
        validateReplySize(form, answerParams)
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
            val questionTemplate = form.questionTemplates.getById(param.questionTemplateId)
            val latestSnapshot = questionTemplate.getLatestSnapshot()

            validateAnswer(latestSnapshot, param)

            Answer(
                text = param.text,
                selectableOptionIds = param.selectableOptionIds,
                questionSnapshotId = latestSnapshot.id,
            )
        }
    }

    /**
     * 개별 답변에 대한 모든 유효성 검사를 수행하는 진입점입니다.
     */
    private fun validateAnswer(questionSnapshot: QuestionSnapshot, param: AnswerParam) {
        validateAnswerByType(questionSnapshot.type, param)
        validateSelectedOptions(questionSnapshot, param)
    }

    /**
     * 정책: 설문에 대한 답변의 개수는 설문에 포함된 질문의 개수와 일치해야 합니다.
     */
    private fun validateReplySize(form: Form, answerParams: List<AnswerParam>) {
        if (form.questionTemplates.size() != answerParams.size) {
            throw CustomException(
                ErrorCode.INVALID_ANSWER_SIZE.withArgs(
                    form.id,
                    form.questionTemplates.size(),
                    answerParams.size
                )
            )
        }
    }

    /**
     * 정책: 선택형 질문의 경우, 제출된 옵션 ID는 반드시 질문에 정의된 옵션이어야 합니다.
     */
    private fun validateSelectedOptions(snapshot: QuestionSnapshot, param: AnswerParam) {
        if (param.selectableOptionIds.isEmpty()) return

        val validOptionIds = snapshot.selectableOptionIds().toSet()
        val invalidIds = param.selectableOptionIds.filterNot { it in validOptionIds }

        if (invalidIds.isNotEmpty()) {
            throw CustomException(
                ErrorCode.INVALID_SELECTABLE_OPTION.withArgs(
                    param.questionTemplateId,
                    invalidIds.joinToString(", ")
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
    private fun validateAnswerByType(questionType: InputType, param: AnswerParam) {
        if (param.text.isNotBlank() && param.selectableOptionIds.isNotEmpty()) {
            throw CustomException(ErrorCode.INVALID_ANSWER.withArgs(questionType))
        }

        when {
            questionType.isTextType() -> {
                if (param.text.isBlank()) {
                    throw CustomException(ErrorCode.INVALID_TEXT_QUESTION_ANSWER.withArgs(questionType))
                }
            }
            questionType.isSelectableType() -> {
                if (param.selectableOptionIds.isEmpty()) {
                    throw CustomException(ErrorCode.INVALID_SELECTABLE_QUESTION_ANSWER.withArgs(questionType))
                }
                if (questionType.isSingleSelectableType() && param.selectableOptionIds.size > 1) {
                    throw CustomException(ErrorCode.INVALID_SINGLE_SELECTABLE_QUESTION_ANSWER.withArgs(questionType))
                }
            }
        }
    }
}
