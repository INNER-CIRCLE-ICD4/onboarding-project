package com.survey.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "SURVEY_OPTION")
data class SurveyOption(
    @Id
    @Column(name = "ID")
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_ITEM_ID", nullable = false)
    val surveyItem: SurveyItem,

    @Column(name = "OPTION_VALUE", nullable = false)
    val optionValue: String,

    @Column(name = "OPTION_ORDER", nullable = false)
    val optionOrder: Int,

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)
