package com.wlghsp.querybox.domain.response

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import support.domain.BaseRootEntity

@Entity
class Response(
    val surveyId: Long,

    @Embedded
    val answers: Answers? = Answers(),

    @Column(columnDefinition = "TEXT", nullable = false)
    val snapshot: String,

    id: Long = 0L
): BaseRootEntity<Response>(id)