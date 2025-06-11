package fastcampus.onboarding.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "option")
@Getter
@NoArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_seq_gen")
    @SequenceGenerator(name = "option_seq_gen", sequenceName = "option_seq", allocationSize = 1)
    @Column(name="option_seq")
    private Long optionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_seq")
    private Item item;

    @Column(name="option_content",length = 200, nullable = false)
    private String optionContent;
    
    @Builder
    public Option(String optionContent) {
        this.optionContent = optionContent;
    }
    
    // Item 연관관계 설정 메서드
    public void setItem(Item item) {
        this.item = item;
    }
    
    // 옵션 내용 업데이트 메서드
    public void updateContent(String optionContent) {
        this.optionContent = optionContent;
    }
}
