package com.innercircle.survey.survey.application.port.out

import com.innercircle.survey.survey.domain.Survey
import java.util.UUID

interface SurveyRepository {
    fun save(survey: Survey): Survey

    fun findById(id: UUID): Survey?

    fun findAll(): List<Survey>

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
