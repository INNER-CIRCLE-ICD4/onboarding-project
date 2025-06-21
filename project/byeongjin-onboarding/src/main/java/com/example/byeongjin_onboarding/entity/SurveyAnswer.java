package com.example.byeongjin_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey; // 어떤 설문조사에 대한 응답인지

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt; // 응답 제출 시간

    @OneToMany(mappedBy = "surveyAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnswerItem> answerItems = new ArrayList<>(); // 응답 항목 리스트

    // 편의 메서드: AnswerItem 추가 시 양방향 관계 설정
    public void addAnswerItem(AnswerItem answerItem) {
        answerItems.add(answerItem);
        answerItem.setSurveyAnswer(this);
    }
}