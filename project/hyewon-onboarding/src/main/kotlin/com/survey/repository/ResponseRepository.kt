package com.survey.repository

import com.survey.model.Response
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ResponseRepository : JpaRepository<Response, UUID>