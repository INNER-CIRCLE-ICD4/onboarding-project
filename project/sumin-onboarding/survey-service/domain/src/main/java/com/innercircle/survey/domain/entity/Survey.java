package com.innercircle.survey.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    //설문조사 ID(PK)
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    //설문조사 제목
    private String title;

    //설문조사 설명
    private String description;

    //설문받을 항목과 1:N
    @Builder.Default
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<Question>();

    //설문조사 생성자
    private String createdBy;

    //설문조사 생성시간
    private LocalDateTime createdAt;

    //설문조사 수정자
    private String updatedBy;

    //설문조사 수정시간
    private LocalDateTime updatedAt;
}
