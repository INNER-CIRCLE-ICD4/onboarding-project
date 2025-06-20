package com.example.demo.item;

import com.example.demo.item.dto.UpdateItemDto;
import com.example.demo.itemQuestion.ItemQuestion;
import com.example.demo.utill.ItemType;
import com.example.demo.survey.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item")
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "item_no")
    private int itemNo;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "type")
    private ItemType itemType;


    @Column(name = "is_required")
    private boolean isRequired;

    @OneToMany(mappedBy = "item",fetch = FetchType.LAZY)
    private List<ItemQuestion> itemQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    public void updateItem(UpdateItemDto updateItemDto) {
        if(updateItemDto.getItemName() != null ) this.itemName = updateItemDto.getItemName();
        if(updateItemDto.getItemDescription() != null ) this.itemDescription = updateItemDto.getItemDescription();
        if(updateItemDto.getItemType() != null ) this.itemType = updateItemDto.getItemType();
        this.isRequired = updateItemDto.isRequired();

//        if(updateItemDto.getItemQuestionList() != null) {
//           ItemQuestion question = itemQuestion.stream()
//                   .filter(itemQuestion1 -> itemQuestion1.getQuestionNo() == updateItemDto.getItemNo())
//                   .findFirst()
//                   .orElseThrow(() -> new IllegalArgumentException("ItemQuestion not found for itemNo: " + updateItemDto.getItemNo()));
////            question.updateItemQuestion(updateItemDto.getItemQuestionList());
//
//        }
        this.updateAt = LocalDateTime.now();
    }
}



