package fc.icd.baulonboarding.answer.model.entity;

import fc.icd.baulonboarding.common.exception.InvalidParamException;
import fc.icd.baulonboarding.survey.model.entity.SurveyItemOption;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answer_item_options")
public class AnswerItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answer_item_id")
    private AnswerItem answerItem;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private SurveyItemOption surveyItemOption;

    private String content;

    @Builder
    public AnswerItemOption(AnswerItem answerItem,
                            SurveyItemOption surveyItemOption,
                            String content
    ) {
        if (answerItem == null) throw new InvalidParamException("Entity AnswerItemOption.answerItem");
        if (surveyItemOption == null) throw new InvalidParamException("Entity AnswerItemOption.surveyItemOption");
        if (!StringUtils.hasText(content)) throw new InvalidParamException("Entity AnswerItemOption.content");

        this.answerItem = answerItem;
        this.surveyItemOption = surveyItemOption;
        this.content = content;
    }


}
