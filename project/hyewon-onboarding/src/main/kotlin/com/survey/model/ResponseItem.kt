package com.survey.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "RESPONSE_ITEM")
data class ResponseItem(
    @Id
    @Column(name = "ID")
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESPONSE_ID", nullable = false)
    val response: Response,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_ITEM_ID", nullable = false)
    @JsonBackReference  // SurveyItem → Response 관계의 자식
    val surveyItem: SurveyItem,

    @Column(name = "ANSWER", nullable = false)
    val answer: String,

    @ManyToMany
    @JoinTable(
        name = "RESPONSE_ITEM_OPTION",
        joinColumns = [JoinColumn(name = "RESPONSE_ITEM_ID")],
        inverseJoinColumns = [JoinColumn(name = "SURVEY_OPTION_ID"),]
    )
    val selectedOptions: List<SurveyOption>,

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
) {
    constructor(): this(
        id = UUID.randomUUID(),
        response = Response(),
        surveyItem = SurveyItem(),
        answer = "",
        selectedOptions = listOf(),
        createDt = LocalDateTime.now(),
        updateDt = LocalDateTime.now()
    )
}
