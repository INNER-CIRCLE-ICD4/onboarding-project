package com.okdori.surveyservice.domain

import com.okdori.surveyservice.domain.base.BaseTimeEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * packageName    : com.okdori.surveyservice.domain
 * fileName       : SurveyResponse
 * author         : okdori
 * date           : 2025. 6. 11.
 * description    :
 */

@Table(name = "t_survey_response")
@Entity
class SurveyResponse(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    var survey: Survey
): BaseTimeEntity() {
    @Id @Tsid
    @Column(name = "response_id", columnDefinition = "CHAR(13)")
    var id: String? = null

    @Column(columnDefinition = "VARCHAR(100)")
    var responseUser: String? = null
}
