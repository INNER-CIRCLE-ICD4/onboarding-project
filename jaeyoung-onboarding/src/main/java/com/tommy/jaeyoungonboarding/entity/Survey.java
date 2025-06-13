package com.tommy.jaeyoungonboarding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "survey_master")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Survey {

    // 고유값
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID surveyId;

    // 설문조사 이름
    @Column(name = "survey_title")
    private String surveyTitle;

    // 설문조사 설명
    @Column(name = "survey_description")
    private String surveyDescription;

    /*
    * 설문 받을 항목에 대한 고민
    * SurveyItem과 연관관계를 맺게 되는데 그렇게 되면 트래픽이 많아졌을 시
    * 쿼리의 조회 수가 많이 증가하게 됨
    * 그렇기 때문에 얕은 연관관계를 위해 SurveyItem Entity에 surveyId 값을 컬럼으로 넣어줌
    * */
}
