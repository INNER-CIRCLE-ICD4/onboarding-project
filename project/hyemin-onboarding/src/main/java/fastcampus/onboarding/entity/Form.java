package fastcampus.onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "form")
@Getter
@NoArgsConstructor
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_seq_gen")
    @SequenceGenerator(name = "form_seq_gen", sequenceName = "form_seq", allocationSize = 1)
    private Long form_seq;

    @Column(name="form_title", nullable = false, length = 200)
    private String formTitle; // 설문조사 이름

    @Column(name="form_content",length = 1000)
    private String formContent; // 설문조사 설명

    @Column(name="created_at")
    private LocalDateTime createdAt; // 설문조사 설명

    @Column(name="updated_at")
    private LocalDateTime updatedAt; // 설문조사 설명

}
