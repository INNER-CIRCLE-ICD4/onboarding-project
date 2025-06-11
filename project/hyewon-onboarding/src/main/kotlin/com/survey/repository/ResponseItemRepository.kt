package com.survey.repository

import com.survey.model.ResponseItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ResponseItemRepository : JpaRepository<ResponseItem, UUID>