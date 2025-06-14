package com.donghwan.onboarding.model.time

import java.time.LocalDateTime

interface CreateTimeProps {
    val createdAt: LocalDateTime
}

interface UpdatedTimeProps {
    val updatedAt: LocalDateTime
}

interface SubmittedTimeProps {
    val submittedAt: LocalDateTime
}