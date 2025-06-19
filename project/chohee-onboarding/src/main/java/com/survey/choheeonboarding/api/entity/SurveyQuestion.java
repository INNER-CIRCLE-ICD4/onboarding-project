package com.survey.choheeonboarding.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestion {

    @Id
    String id;
    String questionText;
    String questionDescription;
    String questionType;
    String isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Survey survey;


}
