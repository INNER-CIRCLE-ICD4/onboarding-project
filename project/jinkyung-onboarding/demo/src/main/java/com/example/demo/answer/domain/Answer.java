package com.example.demo.answer.domain;

import com.example.demo.survey.domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "answer")
    private List<ItemAnswer> itemAnswers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

//    @OneToMany(mappedBy = "surveyAnswers",fetch = FetchType.LAZY)
//    private List<SurveyAnswer> surveyAnswer = new ArrayList<>();

}
