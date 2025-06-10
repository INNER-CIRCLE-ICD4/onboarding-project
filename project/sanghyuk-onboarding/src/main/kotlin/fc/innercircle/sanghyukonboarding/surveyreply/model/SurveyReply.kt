package fc.innercircle.sanghyukonboarding.surveyreply.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
open class SurveyReply(
    surveyId: Long,
    responseDate: LocalDateTime? = null,
    createdBy: String,
) : BaseEntity(createdBy) {

    @Column(nullable = false, columnDefinition = "bigint not null comment '설문 ID'")
    var surveyId: Long = surveyId
        protected set

    @Column(nullable = false, columnDefinition = "datetime not null comment '설문 응답일자'")
    var responseDate: LocalDateTime? = responseDate
        protected set
}
