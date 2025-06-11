package com.example.demo.item;

import com.example.demo.utill.ItemType;
import com.example.demo.survey.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item")
public class Item {

    @Id
    @Column(name = "itemNo")
    @GeneratedValue
    private Long itemNo;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "itemDescription")
    private String itemDescription;

    @Column(name = "type")
    private ItemType itemType;

    @Column(name = "itemValue")
    private String itemValue;
    
    @OneToMany
    private List<ItemQuestion> itemQuestion;

    @Column(name = "isRequired")
    private boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyNo")
    private Survey survey;


    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "ItemQuestion") //항목 입력 형태
    public static class ItemQuestion{

        @Id
        @Column(name = "questionNo")
        @GeneratedValue
        private Long questionNo;

        @Column(name = "question")
        private String question;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "itemNo")
        private Item item;



        public static ItemQuestion toEntity(String itemOption) {
            return ItemQuestion.builder()
                    .question(itemOption)
                    .build();
        }
    }
}



