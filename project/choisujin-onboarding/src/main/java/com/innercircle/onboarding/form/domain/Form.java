package com.innercircle.onboarding.form.domain;

import com.innercircle.onboarding.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@ToString
@Entity
@Getter
@NoArgsConstructor
@Table(name = "form")
public class Form extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "PK")
    @Column(name = "seq", nullable = false)
    private Long seq;

    @Comment("UUID (외부조회용 식별자) ")
    @Column(name = "uuid", nullable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Comment("제목")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Comment("설명")
    @Lob
    @Column(name = "description")
    private String description;

    @Builder
    public Form(Long seq, UUID uuid, String title, String description) {
        this.seq = seq;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
    }

}
