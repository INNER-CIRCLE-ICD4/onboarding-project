package com.wlghsp.querybox.domain.response

import jakarta.persistence.Entity
import jakarta.persistence.Lob
import support.domain.BaseEntity

@Entity
class ResponseSnapshot(
    val surveyId: Long,
    @Lob
    val snapshot: String,

    id: Long = 0L
): BaseEntity() {

}
