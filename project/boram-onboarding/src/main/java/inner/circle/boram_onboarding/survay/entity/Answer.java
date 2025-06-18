package inner.circle.boram_onboarding.survay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Answer {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Response response;
    private String questionName; // 질문 이름 (questionId 대신 name 저장)
    @Lob
    @Column(name = "answer_value")
    private String value;        // 답변 값 (JSON/Text)
}