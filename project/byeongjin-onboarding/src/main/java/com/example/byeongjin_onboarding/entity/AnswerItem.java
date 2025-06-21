package com.example.byeongjin_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "answer_items")
@Getter
@Setter
@NoArgsConstructor
public class AnswerItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_item_id", nullable = false)
    private FormItem formItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_answer_id", nullable = false)
    private SurveyAnswer surveyAnswer;

    @Column(name = "answer_content", columnDefinition = "TEXT")
    private String answerContent;

    public AnswerItem(FormItem formItem, String answerContent) {
        this.formItem = formItem;
        this.answerContent = answerContent;
    }

    public String getTextAnswer() {
        return this.answerContent != null ? this.answerContent.trim() : "";
    }

    public List<String> getSelectedOptions() {
        if (this.answerContent == null || this.answerContent.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(this.answerContent.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}