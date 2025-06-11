package com.okdori.surveyservice.domain

import com.okdori.surveyservice.domain.base.BaseTimeEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * packageName    : com.okdori.surveyservice.domain
 * fileName       : SurveyItem
 * author         : okdori
 * date           : 2025. 6. 11.
 * description    :
 */

@Table(name = "t_survey_item")
@Entity
class SurveyItem(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    var survey: Survey
): BaseTimeEntity() {
    @Id @Tsid
    @Column(name = "item_id", columnDefinition = "CHAR(13)")
    var id: String? = null

    @Column(nullable = false, columnDefinition = "VARCHAR(200)")
    var itemName = ""

    @Column(columnDefinition = "VARCHAR(2000)")
    var itemDescription: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var itemType: ItemType

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    var isRequired = false

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    var isDeleted = false
}
