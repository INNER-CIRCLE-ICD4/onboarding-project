package com.wlghsp.querybox.repository

import com.wlghsp.querybox.domain.survey.Survey
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface SurveyRepository : JpaRepository<Survey, Long> {

    @EntityGraph(attributePaths = ["questions.values"])
    fun findSurveyWithQuestionsById(@Param("id") id: Long): Survey?
}