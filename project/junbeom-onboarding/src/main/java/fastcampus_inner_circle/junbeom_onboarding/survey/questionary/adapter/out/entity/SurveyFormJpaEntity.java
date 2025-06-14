package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "survey_form")
public class SurveyFormJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String describe;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<SurveyContentJpaEntity> contents;

    @Builder
    protected SurveyFormJpaEntity(Long id, String name, String describe, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.createAt = createAt;
    }


}