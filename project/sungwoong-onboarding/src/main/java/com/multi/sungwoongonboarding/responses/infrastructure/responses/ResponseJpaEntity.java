package com.multi.sungwoongonboarding.responses.infrastructure.responses;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.responses.domain.Answers;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.infrastructure.answers.AnswerJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "responses")
public class ResponseJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long id;

    @Column(name = "form_id", nullable = false)
    private Long formId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Setter
    @Column(name = "form_version", nullable = false)
    private int formVersion;

    @OneToMany(mappedBy = "responseJpaEntity")
    private List<AnswerJpaEntity> answers = new ArrayList<>();

    public static ResponseJpaEntity fromDomain(Responses responses) {

        ResponseJpaEntity responseJpaEntity = new ResponseJpaEntity();
        responseJpaEntity.id = responses.getId();
        responseJpaEntity.formVersion = responses.getFormVersion();
        responseJpaEntity.formId = responses.getFormId();
        responseJpaEntity.userId = responses.getUserId();

        return responseJpaEntity;
    }


    public Responses toDomain() {
        return Responses.builder()
                .id(this.id)
                .formId(this.formId)
                .userId(this.userId)
                .answers(this.answers.stream().map(AnswerJpaEntity::toDomain).toList())
                .createdAt(this.getCreatedAt())
                .build();
    }
}