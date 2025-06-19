package fc.icd.baulonboarding.survey.model.entity;

import fc.icd.baulonboarding.common.exception.InvalidParamException;
import fc.icd.baulonboarding.common.model.entity.AbstractEntity;
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
@Table(name = "surveys")
public class Survey extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "survey", cascade = CascadeType.PERSIST)
    private List<SurveyItem> surveyItemList = new ArrayList<>();

    @Builder
    public Survey(String name, String description){
        if(!StringUtils.hasText(name)) throw new InvalidParamException("Entity Survey.name");
        this.name = name;
        this.description = description;
    }

    public void applyChanges(String name, String description){
        if(!StringUtils.hasText(name)) throw new InvalidParamException("Entity Survey.name");
        this.name = name;
        this.description = description;
    }

}
