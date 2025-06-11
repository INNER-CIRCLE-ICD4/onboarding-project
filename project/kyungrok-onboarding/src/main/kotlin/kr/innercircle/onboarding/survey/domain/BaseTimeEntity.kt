package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : BaseTimeEntity
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}