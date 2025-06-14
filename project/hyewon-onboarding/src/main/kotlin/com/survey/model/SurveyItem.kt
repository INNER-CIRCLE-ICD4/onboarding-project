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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_ITEM_ID", nullable = false)
    val survey: Survey,

    @Column(name = "QUESTION", nullable = false)
    val question: String,

    @Column(name = "TYPE", nullable = false)
    val type: String,

    @Column(name = "ITEM_ORDER", nullable = false)
    val itemOrder: Int,

    @OneToMany(mappedBy = "surveyItem", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val options: List<SurveyOption> = listOf(),

    @OneToMany(mappedBy = "surveyItem", cascade = [CascadeType.ALL], orphanRemoval = true)
    val responseItems: List<ResponseItem> = listOf(),

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)