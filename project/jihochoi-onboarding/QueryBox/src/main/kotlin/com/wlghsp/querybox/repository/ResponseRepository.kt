package com.wlghsp.querybox.repository

import com.wlghsp.querybox.domain.response.Response
import com.wlghsp.querybox.ui.dto.ResponseDto
import org.springframework.data.jpa.repository.JpaRepository

interface ResponseRepository : JpaRepository<Response, Long> {

    fun findAllBySurveyId(surveyId: Long): List<Response>
}