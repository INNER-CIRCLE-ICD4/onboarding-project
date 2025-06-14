package fc.icd.baulonboarding.survey.model.entity;

import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "survey_item_options")
public class SurveyItemOption extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_item_id")
    private SurveyItem surveyItem;

    private String content;

    private Integer ordering;

    private boolean isDeleted;

}
