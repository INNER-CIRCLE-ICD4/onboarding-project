package com.okdori.surveyservice.domain

import com.okdori.surveyservice.domain.base.BaseTimeEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * packageName    : com.okdori.surveyservice.domain
 * fileName       : survey
 * author         : okdori
 * date           : 2025. 6. 11.
 * description    :
 */

@Table(name = "t_survey")
@Entity
class Survey: BaseTimeEntity() {
    @Id @Tsid
    @Column(name = "survey_id", columnDefinition = "CHAR(13)")
    var id: String? = null

    @Column(nullable = false, columnDefinition = "VARCHAR(200)")
    var surveyName = ""

    @Column(columnDefinition = "VARCHAR(2000)")
    var surveyDescription: String? = null

    fun updateSurvey(surveyName: String?, surveyDescription: String?) {
        surveyName?.let { this.surveyName = surveyName }
        surveyDescription?.let { this.surveyDescription = surveyDescription }
    }
}
