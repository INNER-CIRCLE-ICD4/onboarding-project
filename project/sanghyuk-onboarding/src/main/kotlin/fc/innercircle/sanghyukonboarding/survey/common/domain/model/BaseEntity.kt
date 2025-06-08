package fc.innercircle.sanghyukonboarding.survey.common.domain.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(
    createdBy: String,
) {
    @Column(name = "created_by", nullable = false, length = 50, columnDefinition = "varchar(50) comment '생성자'")
    val createdBy: String = createdBy

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime comment '생성 일시'")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_by", nullable = true, length = 50, columnDefinition = "varchar(50) comment '수정자'")
    var updatedBy: String? = null
        protected set

    @Column(name = "updated_at", nullable = true, columnDefinition = "datetime comment '수정 일시'")
    var updatedAt: LocalDateTime? = null
        protected set

    fun update(updatedBy: String) {
        this.updatedBy = updatedBy
        this.updatedAt = LocalDateTime.now()
    }
}
