package com.innercircle.onboarding.changzune_onboarding.survey.domain;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity //이클래스는 DB테이블이라고 생각
public class Survey {

    @Id //기본 Primary Key로 지정하기위해서 사용

    //기본키를 자동으로 생성하는 방식지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;         // 설문조사 이름
    private String description;   // 설문조사 설명

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    // getter/setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}