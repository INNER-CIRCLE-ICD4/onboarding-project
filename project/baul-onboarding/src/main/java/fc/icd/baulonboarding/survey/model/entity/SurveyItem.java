package fc.icd.baulonboarding.survey.model.entity;

import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import fc.icd.baulonboarding.survey.model.code.InputType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_items")
public class SurveyItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    private boolean isRequired;

    private boolean isDeleted;

    private Integer ordering;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyItem", cascade = CascadeType.PERSIST)
    private List<SurveyItemOption> surveyItemOptionList = new ArrayList<>();


}
