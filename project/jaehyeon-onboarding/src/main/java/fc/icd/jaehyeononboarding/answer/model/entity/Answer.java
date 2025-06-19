package fc.icd.jaehyeononboarding.answer.model.entity;

import fc.icd.jaehyeononboarding.common.converter.AnswerDTOListJsonConverter;
import fc.icd.jaehyeononboarding.survey.model.entity.QuestionGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_group_id")
    private QuestionGroup questionGroup;

    @Column(nullable = false)
    @Convert(converter = AnswerDTOListJsonConverter.class)
    private List<?> text;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Answer create(QuestionGroup questionGroup, List<?> text) {
        Answer answer = new Answer();
        answer.setQuestionGroup(questionGroup);
        answer.setText(text);
        answer.setCreatedAt(LocalDateTime.now());
        return answer;
    }
}
