package fc.icd.baulonboarding.survey.model.entity;

import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "surveys")
public class Survey extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "survey", cascade = CascadeType.PERSIST)
    private List<SurveyItem> surveyItemsList = new ArrayList<>();

}
