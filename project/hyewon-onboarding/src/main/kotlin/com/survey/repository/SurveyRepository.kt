package com.survey.repository

import com.survey.model.Survey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SurveyRepository : JpaRepository<Survey, UUID>