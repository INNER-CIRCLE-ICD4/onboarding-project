package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "survey_content_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
public class SurveyContentOptionJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    @Setter
    private SurveyContentJpaEntity content;

    @Column(length = 100)
    private String text;

    @Builder
    protected SurveyContentOptionJpaEntity(Long id, String text) {
        this.id = id;
        this.text = text;
    }
} 