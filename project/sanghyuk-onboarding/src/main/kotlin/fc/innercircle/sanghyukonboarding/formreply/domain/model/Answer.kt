package fc.innercircle.sanghyukonboarding.formreply.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType

class Answer(
    val id: String = "",
    val answer: String = "",
    val selectableOptionIds: List<String> = emptyList(),
    val questionSnapshotId: String,
    val formReplyId: String = "",
) {

    constructor(
        questionInputType: InputType,
        id: String = "",
        answer: String = "",
        selectableOptionIds: List<String> = emptyList(),
        questionSnapshotId: String,
        formReplyId: String = ""
    ): this(
        id = id,
        answer = answer,
        selectableOptionIds = selectableOptionIds,
        questionSnapshotId = questionSnapshotId,
        formReplyId = formReplyId
    ) {
        validateAnswersOrThrows(questionInputType)
    }


    /**
     * 비즈니스 정책 검사
     * 1. 텍스트형 질문의 답변은 비어있을 수 없다.
     * 2. 선택형 질문의 답변은 최소 하나 이상의 선택지를 포함해야 한다.
     * 3. 단일 선택형 질문의 답변은 최대 하나의 선택지만 포함해야 한다.
     */
    private fun validateAnswersOrThrows(questionInputType: InputType) {
        if (answer.isNotBlank() && selectableOptionIds.isNotEmpty()) {
            throw CustomException(ErrorCode.INVALID_ANSWER.withArgs(questionInputType))
        }
        if (questionInputType.isTextType() && answer.isBlank()) {
            throw CustomException(ErrorCode.INVALID_TEXT_QUESTION_ANSWER.withArgs(questionInputType))
        }
        if (questionInputType.isSelectableType() && selectableOptionIds.isEmpty()) {
            throw CustomException(ErrorCode.INVALID_SELECTABLE_QUESTION_ANSWER.withArgs(questionInputType))
        }
        if (questionInputType.isSingleSelectableType() && selectableOptionIds.size > 1) {
            throw CustomException(ErrorCode.INVALID_SINGLE_SELECTABLE_QUESTION_ANSWER.withArgs(questionInputType))
        }
    }

    fun isNew(): Boolean {
        return id.isBlank()
    }

    fun isFormReplyOf(formReply: FormReply): Boolean {
        return formReplyId == formReply.id
    }
}
