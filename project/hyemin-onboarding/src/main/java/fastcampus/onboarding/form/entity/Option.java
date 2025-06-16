package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "option")
@Getter
@Setter
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
}