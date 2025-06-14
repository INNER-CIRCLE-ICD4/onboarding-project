package onboardingproject.project.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * packageName : onboardingproject.project.domain
 * fileName    : BaseEntity
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    lateinit var lastModifiedDate: LocalDateTime
}
