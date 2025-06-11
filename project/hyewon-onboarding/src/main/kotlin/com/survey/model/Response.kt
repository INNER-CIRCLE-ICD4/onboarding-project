package com.survey.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "RESPONSE")
data class Response(
    @Id
    @Column(name = "ID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "SURVEY_ID", nullable = false)
    val surveyId: UUID,

    @Column(name = "RESPONDENT")
    val respondent: String? = null,

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)
