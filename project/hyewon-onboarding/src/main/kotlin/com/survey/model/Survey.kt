package com.survey.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name="SURVEY")
data class Survey(
    @Id
    @Column(name="ID")
    val id: UUID = UUID.randomUUID(),

    @Column(name="NAME", nullable = false)
    val title: String,

    @Column(name="DESCRIPTION")
    val description: String? = null,

    @OneToMany(mappedBy = "survey", cascade=[(CascadeType.ALL)], orphanRemoval = true)
    @JsonManagedReference   // Survey → SurveyItem 관계의 부모
    val items: List<SurveyItem> = listOf(),

    @OneToMany(mappedBy = "survey", cascade=[(CascadeType.ALL)], orphanRemoval = true)
    @JsonManagedReference   // Survey → Response 관계의 부모
    val responses: List<Response> = listOf(),

    @Column(name="CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name="UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = UUID.randomUUID(),
        title = "",
        description = null,
        items = listOf(),
        responses = listOf(),
        createDt = LocalDateTime.now(),
        updateDt = LocalDateTime.now()
    )
}