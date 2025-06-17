package com.survey.soyoung_onboarding.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Survey {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    
    /**
     * 설문 제목
     */
    private String title;

    /**
     * 설문 설명
     */
    private String description;

    /**
     *  질문 항목
     */
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    private Date reg_date;

    private Date update_date;

}