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
 * fileName       : SurveyItemOption
 * author         : okdori
 * date           : 2025. 6. 11.
 * description    :
 */

@Table(name = "t_survey_item_option")
@Entity
class SurveyItemOption(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var surveyItem: SurveyItem
): BaseTimeEntity() {
    @Id @Tsid
    @Column(name = "option_id", columnDefinition = "CHAR(13)")
    var id: String? = null

    @Column(nullable = false, columnDefinition = "VARCHAR(200)")
    var optionName = ""

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    var isDeleted = false
}
