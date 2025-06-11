package com.survey.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "SURVEY_ITEM")
data class SurveyItem(
    @Id
    @Column(name = "ID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "SURVEY_ID", nullable = false)
    val surveyId: UUID,

    @Column(name = "QUESTION", nullable = false)
    val question: String,

    @Column(name = "TYPE", nullable = false)
    val type: String,

    @Column(name = "ITEM_ORDER", nullable = false)
    val itemOrder: Int,

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)