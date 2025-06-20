package com.survey.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
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
    @JsonBackReference  // Response → Survey 관계의 자식
    val survey: Survey,

    @Column(name = "RESPONDENT")
    val respondent: String? = null,

    @Column(name = "UPDATE_DT")
    val updateDt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "response", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference   // Response → ResponseItem 관계의 부모
    val responseItems: MutableList<ResponseItem> = mutableListOf(),

    @Column(name = "CREATE_DT")
    val createDt: LocalDateTime = LocalDateTime.now()
) {
    constructor(): this(
        id = UUID.randomUUID(),
        survey = Survey(),
        respondent = null,
        responseItems = mutableListOf(),
        createDt = LocalDateTime.now(),
        updateDt = LocalDateTime.now()
    )
}
