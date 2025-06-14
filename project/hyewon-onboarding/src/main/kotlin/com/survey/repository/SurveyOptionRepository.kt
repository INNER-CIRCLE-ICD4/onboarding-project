package com.survey.repository

import com.survey.model.SurveyOption
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SurveyOptionRepository : JpaRepository<SurveyOption, UUID>