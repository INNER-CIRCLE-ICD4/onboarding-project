package formService.domain

import java.time.LocalDateTime

class Answer(
    val id: String,
    val userEmail: String,
    val submittedAt: LocalDateTime,
    val values: List<QuestionAnswer>,
)
