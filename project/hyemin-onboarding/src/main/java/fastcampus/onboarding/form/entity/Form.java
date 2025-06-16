package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime createdAt; // 생성일시

    @Column(name="updated_at")
    private LocalDateTime updatedAt; // 수정일시

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY)
    private List<FormResponse> responses = new ArrayList<>();

    @Builder
    public Form(String formTitle, String formContent) {
        this.formTitle = formTitle;
        this.formContent = formContent;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateForm(String formTitle, String formContent) {
        this.formTitle = formTitle;
        this.formContent = formContent;
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setForm(this);
    }
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
}