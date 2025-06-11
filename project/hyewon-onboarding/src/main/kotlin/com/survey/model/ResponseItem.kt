package com.survey.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "RESPONSE_ITEM")
data class ResponseItem(
    @Id
    @Column(name = "ID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "RESPONSE_ID", nullable = false)
    val responseId: UUID,

    @Column(name = "SURVEY_ITEM_ID", nullable = false)
    val surveyItemId: UUID,

    @Column(name = "ANSWER", nullable = false)
    val answer: String,

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)
