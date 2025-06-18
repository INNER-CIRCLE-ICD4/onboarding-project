package com.wlghsp.querybox.repository

import com.wlghsp.querybox.domain.response.Response
import org.springframework.data.jpa.repository.JpaRepository

interface ResponseRepository : JpaRepository<Response, Long> {
}