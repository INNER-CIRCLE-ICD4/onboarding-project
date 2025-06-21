package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class SelectableOptionIds(
    @Column(
        name = "selectable_option_id",
        nullable = false,
        columnDefinition = "char(26) not null comment '선택형 옵션 ID (선택형 입력 타입에만 값이 존재)'"
    )
    val selectableOptionIds: List<String> = emptyList(),
) {
    fun toList(): List<String> {
        return selectableOptionIds
    }
}
