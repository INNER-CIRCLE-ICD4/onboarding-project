package fastcampus.onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "item_seq", allocationSize = 1)
    private Long itemSeq;

    @ManyToOne
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

}
