package com.survey.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
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
    @JoinColumn(name = "SURVEY_ID", nullable = false)
    @JsonBackReference  // SurveyItem → Survey 관계의 자식
    val survey: Survey,

    @Column(name = "QUESTION", nullable = false)
    val question: String,

    @Column(name = "TYPE", nullable = false)
    val type: String,

    @Column(name = "ITEM_ORDER", nullable = false)
    val itemOrder: Int,

    @OneToMany(mappedBy = "surveyItem", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference   // SurveyItem → SurveyOption 관계의 부모
    val options: MutableList<SurveyOption> = mutableListOf(),

    @OneToMany(mappedBy = "surveyItem", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference   // SurveyItem → ResponseItem 관계의 부모
    val responseItems: List<ResponseItem> = listOf(),

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = UUID.randomUUID(),
        survey = Survey(),
        question = "",
        type = "",
        itemOrder = 0,
        options = mutableListOf(),
        createDt = LocalDateTime.now(),
        updateDt = LocalDateTime.now()
    )
}