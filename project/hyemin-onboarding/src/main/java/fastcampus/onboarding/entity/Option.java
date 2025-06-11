package fastcampus.onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "option")
@Getter
@NoArgsConstructor
public class Option {

    @Id
    @Column(name="option_seq")
    private Long optionSeq;

    @ManyToOne
    @JoinColumn(name="item_seq")
    private Item item;

    @Column(name="option_content",length = 200, nullable = false)
    private String optionContent;

}
