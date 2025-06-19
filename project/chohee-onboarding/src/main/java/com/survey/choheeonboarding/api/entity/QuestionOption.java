package com.survey.choheeonboarding.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    String id;
    String surveyId;
    String optionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    SurveyQuestion question;

}
