package formService.adapter.port.outbound.persistence.jpa

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import jakarta.persistence.Transient
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractEntity(
    id: String,
) : Persistable<String> {
    @Id
    @Column(columnDefinition = "char(13)")
    private val id: String = id

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @Transient
    private var _isNew = createdAt == null

    override fun getId(): String = id

    override fun isNew(): Boolean = _isNew

    @PostLoad
    @PrePersist
    fun markNotNow() {
        _isNew = false
    }
}
