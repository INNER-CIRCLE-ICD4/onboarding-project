package com.survey.model

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
    val items: List<SurveyItem> = listOf(),

    @OneToMany(mappedBy = "survey", cascade=[(CascadeType.ALL)], orphanRemoval = true)
    val responses: List<Response> = listOf(),

    @Column(name="CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name="UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)