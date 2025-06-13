package fc.innercircle.sanghyukonboarding.common.domain.model

import fc.innercircle.sanghyukonboarding.common.infrastructure.numbering.IdGeneratingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(IdGeneratingEntityListener::class)
abstract class BaseEntity(
    createdBy: String,
) : Identifiable {

    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    override var id: String = "" // ID는 서브클래스에서 SnowflakeIdGenerator 등을 통해 생성될 예정

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
}
