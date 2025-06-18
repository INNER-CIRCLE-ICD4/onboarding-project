package com.group.surveyapp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
public class AnswerSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Survey와 N:1 (응답지→설문)
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    // 제출일시
    private LocalDateTime submittedAt;

    // 답변 리스트
    @OneToMany(mappedBy = "answerSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    // 제출일시 세터
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
