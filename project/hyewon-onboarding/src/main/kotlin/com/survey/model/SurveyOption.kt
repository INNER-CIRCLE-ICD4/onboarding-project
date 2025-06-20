package com.survey.model

import com.fasterxml.jackson.annotation.JsonBackReference
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
    @JsonBackReference  // SurveyItem → SurveyOption 자식 관계
    val surveyItem: SurveyItem,

    @Column(name = "OPTION_VALUE", nullable = false)
    val optionValue: String,

    @Column(name = "OPTION_ORDER", nullable = false)
    var optionOrder: Int,

    @Column(name = "ACTIVE", nullable = false)
    var active: Boolean = true,

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    var updateDt: LocalDateTime = LocalDateTime.now()
) {
    constructor(): this(
        id = UUID.randomUUID(),
        surveyItem = SurveyItem(),
        optionValue = "",
        optionOrder = 0,
        active = true,
        createDt = LocalDateTime.now(),
        updateDt = LocalDateTime.now()
    )
}
