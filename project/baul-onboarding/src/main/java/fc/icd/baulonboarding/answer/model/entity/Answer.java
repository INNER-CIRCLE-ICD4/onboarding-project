package fc.icd.baulonboarding.answer.model.entity;

import fc.icd.baulonboarding.common.exception.InvalidParamException;
import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
import fc.icd.baulonboarding.survey.model.entity.Survey;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answers")
public class Answer extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "answer", cascade = CascadeType.PERSIST)
    private List<AnswerItem> answerItemList = new ArrayList<>();

    @Builder
    public Answer(Survey survey){
        if (survey == null) throw new InvalidParamException("Entity Answer.survey");
        if (!StringUtils.hasText(survey.getName())) throw new InvalidParamException("Entity Answer.survey.name");

        this.survey = survey;
        this.name = survey.getName();
        this.description = survey.getDescription();
    }

}
