package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;

import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;


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
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int version;
}
