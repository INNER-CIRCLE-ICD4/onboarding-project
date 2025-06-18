package inner.circle.boram_onboarding.survay.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import inner.circle.boram_onboarding.survay.enumerated.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Survey survey;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private QuestionType type; // SHORT, LONG, SINGLE_CHOICE, MULTIPLE_CHOICE
    private Boolean required;
    @Lob
    private String options; // JSON 배열 형태로 저장 (SINGLE/MULTIPLE일 때만)
}
