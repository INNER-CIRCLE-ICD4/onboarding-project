package fc.innercircle.sanghyukonboarding.surveyreply.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
open class SurveyReply(
    surveyId: Long,
    responseDate: LocalDateTime? = null,
    createdBy: String,
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L

    @Column(nullable = false, columnDefinition = "bigint not null comment '설문 ID'")
    var surveyId: Long = surveyId
        protected set

    @Column(nullable = false, columnDefinition = "datetime not null comment '설문 응답일자'")
    var responseDate: LocalDateTime? = responseDate
        protected set
}
