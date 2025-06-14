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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_ID", nullable = false)
    val survey: Survey,

    @Column(name = "RESPONDENT")
    val respondent: String? = null,

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "response", cascade = [CascadeType.ALL], orphanRemoval = true)
    val responseItems: List<ResponseItem> = listOf(),

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now()
)
