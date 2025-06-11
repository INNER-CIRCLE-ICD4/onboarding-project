package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;

import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author khm0813
 * - 설문조사 테이블
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 하나의 설문조사에는 여러 질문이 생성 될 수 있다.
     */
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

}
