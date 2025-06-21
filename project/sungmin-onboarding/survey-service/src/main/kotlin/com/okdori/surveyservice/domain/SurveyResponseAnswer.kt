package com.okdori.surveyservice.domain

import com.okdori.surveyservice.domain.base.BaseTimeEntity
import io.hypersistence.utils.hibernate.id.Tsid
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Type

/**
 * packageName    : com.okdori.surveyservice.domain
 * fileName       : SurveyResponseAnswer
 * author         : okdori
 * date           : 2025. 6. 11.
 * description    :
 */

@Table(name = "t_survey_response_answer")
@Entity
class SurveyResponseAnswer(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    var surveyResponse: SurveyResponse,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var surveyItem: SurveyItem
): BaseTimeEntity() {
    @Id @Tsid
    @Column(name = "answer_id", columnDefinition = "CHAR(13)")
    var id: String? = null

    @Column(nullable = false, columnDefinition = "VARCHAR(200)")
    var itemName = ""

    @Type(JsonType::class)
    @Column(columnDefinition = "JSON")
    var answer: List<String> = emptyList()
}
