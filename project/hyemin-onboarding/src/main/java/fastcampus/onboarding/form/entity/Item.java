package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "item_seq", allocationSize = 1)
    private Long itemSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="form_seq", nullable = false)
    private Form form;

    @Column(name="item_title",length = 1000, nullable = false)
    private String itemTitle;

    @Column(name="item_content",length = 1000, nullable = false)
    private String itemContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    @Column(name="is_required", nullable = false)
    private boolean isRequired; // 항목 필수 여부
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();
    
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemResponse> responses = new ArrayList<>();

    @Builder
    public Item(String itemTitle, String itemContent, ItemType itemType, boolean isRequired) {
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
        this.itemType = itemType;
        this.isRequired = isRequired;
    }
    
    // Form 연관관계 설정 메서드
    public void setForm(Form form) {
        this.form = form;
    }
    
    // 옵션 추가 메서드
    public void addOption(Option option) {
        this.options.add(option);
        option.setItem(this);
    }
    
    // 항목 업데이트 메서드
    public void updateItem(String itemTitle, String itemContent, ItemType itemType, boolean isRequired) {
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
        this.itemType = itemType;
        this.isRequired = isRequired;
    }
}
