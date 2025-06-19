package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.domain.FormsHistory;
import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "forms")
public class FormsJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "form_version")
    private int version = 1;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "formsJpaEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<QuestionJpaEntity> questions = new ArrayList<>();

    public static FormsJpaEntity fromDomain(Forms form) {
        FormsJpaEntity formsJpaEntity = new FormsJpaEntity();
        formsJpaEntity.id = form.getId();
        formsJpaEntity.title = form.getTitle();
        formsJpaEntity.description = form.getDescription();
        formsJpaEntity.version = form.getVersion();
        // 연관관계 매핑
        mappingEntityFromDomain(form, formsJpaEntity);
        return formsJpaEntity;
    }

    public FormsJpaEntity update(Forms form) {

        this.id = form.getId();
        this.title = form.getTitle();
        this.description = form.getDescription();
        //버전 업
        this.version = form.getVersion();
        // 질문 모두 삭제 처리
        this.questions.forEach(QuestionJpaEntity::delete);
        // 도메인 > 엔티티 매핑
        mappingEntityFromDomain(form, this);
        return this;
    }

    public Forms toDomain() {
        return getFormsBuilder()
                .build();
    }

    public Forms toDomainWithHistories(List<FormsHistory> formsHistory) {
        return getFormsBuilder()
                .formsHistories(formsHistory)
                .build();
    }

    private static void mappingEntityFromDomain(Forms form, FormsJpaEntity formsJpaEntity) {
        if (form.getQuestions() != null && !form.getQuestions().isEmpty()) {
            form.getQuestions().forEach(question -> {
                QuestionJpaEntity questionJpaEntity = QuestionJpaEntity.fromDomain(question);
                questionJpaEntity.mappingFormJpaEntity(formsJpaEntity);
            });
        }
    }

    private Forms.FormsBuilder getFormsBuilder() {
        return Forms.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .questions(this.questions.stream().map(QuestionJpaEntity::toDomain).toList())
                .version(this.version)
                .createdAt(this.getCreatedAt())
                .createdAt(this.getUpdatedAt())
                .userId(this.userId);
    }

}
