package fastcampus.onboarding.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_response")
@Getter
@NoArgsConstructor
public class FormResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_response_seq_gen")
    @SequenceGenerator(name = "form_response_seq_gen", sequenceName = "form_response_seq", allocationSize = 1)
    private Long responseSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_seq", nullable = false)
    private Form form;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "formResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemResponse> itemResponses = new ArrayList<>();

    @Builder
    public FormResponse(Form form) {
        this.form = form;
        this.submittedAt = LocalDateTime.now();
    }

    public void addItemResponse(ItemResponse itemResponse) {
        this.itemResponses.add(itemResponse);
        itemResponse.setFormResponse(this);
    }
} 