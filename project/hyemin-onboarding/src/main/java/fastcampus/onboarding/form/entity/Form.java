package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//설문지
@Entity
@Table(name = "form")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formSeq;

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

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
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
    // 옵션 추가 메서드
    public void addItem(Item item) {
        this.items.add(item);
        item.setForm(this);
    }



}