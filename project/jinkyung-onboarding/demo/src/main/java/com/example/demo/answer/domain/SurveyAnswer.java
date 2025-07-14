//package com.example.demo.answer.domain;
//
//import com.example.demo.survey.domain.Survey;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "survey_answer")
//public class SurveyAnswer {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "survey_answer_id")
//    private Long surveyAnswerId;
//
//    @ManyToOne
//    @JoinColumn(name = "answer_id")
//    private Answer answer;
//
//    @ManyToOne
//    @JoinColumn(name = "survey_id")
//    private Survey survey;
//
//    @CreationTimestamp
//    @Column(name = "create_at")
//    private LocalDateTime createAt;
//
//    private LocalDateTime updateAt;
//
//
//}
