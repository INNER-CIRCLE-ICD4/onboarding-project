package fastcampus.onboarding.form.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "item_response")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ItemResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemResponseSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_seq", nullable = false)
    private FormResponse formResponse;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "item_seq", nullable = false)
    // private Item item;
    @Column(name = "item_seq", nullable = false)
    private Long itemSeq; 

    // 항목의 타입에 따라 텍스트 응답 또는 옵션 응답을 저장
    @Column(name = "text_value", length = 4000)
    private String textValue;

    // // 다중 선택인 경우를 위한 옵션 응답 관계
    // @ManyToMany
    // @JoinTable(
    //     name = "item_response_option",
    //     joinColumns = @JoinColumn(name = "item_response_seq"),
    //     inverseJoinColumns = @JoinColumn(name = "option_seq")
    // )
    // private Set<Option> selectedOptions = new HashSet<>();


    // 선택지(Option)도 마찬가지로
    @ElementCollection
    @CollectionTable(name = "item_response_option_snapshot", joinColumns = @JoinColumn(name = "item_response_seq"))
    private List<OptionSnapshot> selectedOptionSnapshots = new ArrayList<>();
    
    // 응답 시점의 항목 정보 스냅샷 - 항목이 변경되더라도 응답 당시 정보 유지
    @Column(name = "item_title_snapshot", nullable = false)
    private String itemTitleSnapshot;

    @Column(name = "item_content_snapshot", nullable = false)
    private String itemContentSnapshot;

    @Column(name = "item_type_snapshot", nullable = false)
    private String itemTypeSnapshot;

    @Column(name = "is_required_snapshot", nullable = false)
    private Boolean isRequiredSnapshot;


    @Builder
    public ItemResponse(Long itemSeq, Boolean isRequiredSnapshot, String textValue, String itemTitleSnapshot,
                        String itemContentSnapshot, String itemTypeSnapshot) {
        this.itemSeq = itemSeq;
        this.isRequiredSnapshot = isRequiredSnapshot;
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
//    public void addSelectedOption(Option option) {
//        this.selectedOptions.add(option);
//    }
}