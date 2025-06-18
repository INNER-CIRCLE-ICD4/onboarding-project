package inner.circle.boram_onboarding.survay.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Response {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Survey survey;
    private LocalDateTime submittedAt;
    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();
}