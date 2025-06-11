package com.survey.repository

import com.survey.model.SurveyItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SurveyItemRepository : JpaRepository<SurveyItem, UUID>