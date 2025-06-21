package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLock;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OptimisticLock(excluded = true)
    private String name;

    @OptimisticLock(excluded = true)
    private String description;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "survey")
    private List<Question> questions = new ArrayList<>();


    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
