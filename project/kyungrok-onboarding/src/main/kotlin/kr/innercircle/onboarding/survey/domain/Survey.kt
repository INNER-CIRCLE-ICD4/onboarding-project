package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : Survey
 * author      : ckr
 * date        : 25. 6. 11.
 * description :
 */

@Table(name = "survey")
@Entity
class Survey (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null
): BaseTimeEntity()