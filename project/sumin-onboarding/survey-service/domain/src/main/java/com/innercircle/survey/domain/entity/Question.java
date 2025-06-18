package com.innercircle.survey.domain.entity;

import com.innercircle.survey.common.dto.QuestionUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    //질문 ID(PK)
    @Id
    @GeneratedValue
    private UUID id;

    //질문 제목
    private String title;

    //질문 설명
    private String description;

    //질문 유형(단답형/장문형/단일 선택/다중 선택)
    private String type;

    //질문 항목 필수 여부
    private boolean required;

    //설문조사와 N:1
    @Setter
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    //선택 리스트와 1:N
    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<QuestionOption>();

    //질문 생성자
    private String createdBy;

    //질문 생성시간
    private LocalDateTime createdAt;

    //질문 수정자
    private String updatedBy;

    //질문 수정시간
    private LocalDateTime updatedAt;

    public void update(QuestionUpdateDto questionDto) {
        this.title = questionDto.getTitle();
        this.description = questionDto.getDescription();
        this.type = questionDto.getType();
        this.required = questionDto.isRequired();
    }
}