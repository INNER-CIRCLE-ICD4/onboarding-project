package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.QSurveyResponse.surveyResponse
import com.okdori.surveyservice.domain.QSurveyResponseAnswer.surveyResponseAnswer
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.stereotype.Component

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SearchCondition
 * author         : okdori
 * date           : 2025. 6. 21.
 * description    :
 */

@Component
class SearchCondition {
    fun itemName(condition: String?): BooleanExpression? {
        return if (condition.isNullOrBlank()) {
            null
        } else {
            surveyResponseAnswer.itemName.eq(condition)
        }
    }

    fun answer(condition: String?): BooleanExpression? {
        return if (condition.isNullOrBlank()) {
            null
        } else {
            surveyResponseAnswer.answer.contains(condition)
        }
    }

    fun responseUser(condition: String?): BooleanExpression? {
        return if (condition.isNullOrBlank()) {
            null
        } else {
            surveyResponse.responseUser.eq(condition)
        }
    }

    fun answerCondition(itemName: String?, answerValue: String?): BooleanExpression? {
        val conditions = mutableListOf<BooleanExpression>()

        if (!itemName.isNullOrBlank()) {
            conditions.add(
                JPAExpressions.selectOne()
                    .from(surveyResponseAnswer)
                    .where(
                        surveyResponseAnswer.surveyResponse.eq(surveyResponse),
                        surveyResponseAnswer.itemName.containsIgnoreCase(itemName)
                    )
                    .exists()
            )
        }

        if (!answerValue.isNullOrBlank()) {
            conditions.add(
                JPAExpressions.selectOne()
                    .from(surveyResponseAnswer)
                    .where(
                        surveyResponseAnswer.surveyResponse.eq(surveyResponse),
                        surveyResponseAnswer.answer.containsIgnoreCase(answerValue)
                    )
                    .exists()
            )
        }

        return if (conditions.isEmpty()) null
        else conditions.reduce { acc, condition -> acc.and(condition) }
    }
}
