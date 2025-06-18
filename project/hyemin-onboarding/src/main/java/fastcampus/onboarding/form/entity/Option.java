package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//설문 문항의 옵션 ex: 주로 이용하는 서비스? 1. 웹, 2.앱 등등
@Entity
@Table(name = "option")
@Getter
@Setter
@NoArgsConstructor
public class Option {

    @Id
    @Column(name="option_seq")
    private Integer optionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_seq")
    private Item item;

    @Column(name="option_content",length = 200, nullable = false)
    private String optionContent;

    @Builder
    public Option(Item item, Integer optionSeq, String optionContent) {
        this.item = item;
        this.optionSeq = optionSeq;
        this.optionContent = optionContent;
    }

    // Item 연관관계 설정 메서드
    public void setItem(Item item) {
        this.item = item;
    }
}