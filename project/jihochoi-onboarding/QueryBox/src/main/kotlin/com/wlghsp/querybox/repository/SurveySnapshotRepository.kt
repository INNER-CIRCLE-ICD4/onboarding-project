package com.wlghsp.querybox.repository

import com.wlghsp.querybox.domain.response.ResponseSnapshot
import org.springframework.data.jpa.repository.JpaRepository

interface SurveySnapshotRepository : JpaRepository<ResponseSnapshot, Long>