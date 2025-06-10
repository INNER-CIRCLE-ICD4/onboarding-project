package fc.innercircle.jinhoonboarding.dto

import fc.innercircle.jinhoonboarding.enum.QuestionType

data class QuestionDTO (
    val title: String,
    val description: String,
    val questionType: QuestionType,
    val required: Boolean,
    val options: List<String>?
)
