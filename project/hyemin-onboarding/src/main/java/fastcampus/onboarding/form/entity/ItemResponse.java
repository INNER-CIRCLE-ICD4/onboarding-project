package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item_response")
@Getter
@NoArgsConstructor
public class ItemResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_response_seq_gen")
    @SequenceGenerator(name = "item_response_seq_gen", sequenceName = "item_response_seq", allocationSize = 1)
    private Long itemResponseSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_seq", nullable = false)
    private FormResponse formResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_seq", nullable = false)
    private Item item;
    
    // 항목의 타입에 따라 텍스트 응답 또는 옵션 응답을 저장
    @Column(name = "text_value", length = 4000)
    private String textValue;
    
    // 다중 선택인 경우를 위한 옵션 응답 관계
    @ManyToMany
    @JoinTable(
        name = "item_response_option",
        joinColumns = @JoinColumn(name = "item_response_seq"),
        inverseJoinColumns = @JoinColumn(name = "option_seq")
    )
    private Set<Option> selectedOptions = new HashSet<>();
    
    // 응답 시점의 항목 정보 스냅샷 - 항목이 변경되더라도 응답 당시 정보 유지
    @Column(name = "item_title_snapshot", nullable = false)
    private String itemTitleSnapshot;
    
    @Column(name = "item_content_snapshot", nullable = false)
    private String itemContentSnapshot;
    
    @Column(name = "item_type_snapshot", nullable = false)
    private String itemTypeSnapshot;
    
    @Builder
    public ItemResponse(Item item, String textValue, String itemTitleSnapshot, 
                       String itemContentSnapshot, String itemTypeSnapshot) {
        this.item = item;
        this.textValue = textValue;
        this.itemTitleSnapshot = itemTitleSnapshot;
        this.itemContentSnapshot = itemContentSnapshot;
        this.itemTypeSnapshot = itemTypeSnapshot;
    }
    
    // 연관관계 설정 메서드
    public void setFormResponse(FormResponse formResponse) {
        this.formResponse = formResponse;
    }
    
    // 선택 옵션 추가 메서드
    public void addSelectedOption(Option option) {
        this.selectedOptions.add(option);
    }
} 