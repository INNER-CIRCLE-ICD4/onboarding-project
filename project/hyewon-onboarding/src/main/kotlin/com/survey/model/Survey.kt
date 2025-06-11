package com.survey.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
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

    @Column(name="CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now(),

    @Column(name="UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now()
)