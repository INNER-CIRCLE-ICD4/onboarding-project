package com.example.demo.itemQuestion;

import com.example.demo.item.Item;
import com.example.demo.item.dto.UpdateItemQuestionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ItemQuestion") //단일, 다중선택시
public class ItemQuestion{

    @Id
    @Column(name = "itemQuestion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemQuestionId;

    @Column(name = "question")
    private String question;

    @Column(name = "question_no")
    private int questionNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    private LocalDateTime updateAt;



    public static ItemQuestion toEntity(String itemOption) {
        return ItemQuestion.builder()
                .question(
                        ObjectUtils.isEmpty(itemOption)
                                ? null
                                : itemOption
                )
                .build();
    }

    public void updateItemQuestion(UpdateItemQuestionDto questionDto) {
            if (this.questionNo == questionDto.getQuestionNo()) {
                this.question = questionDto.getQuestion();
                this.updateAt = LocalDateTime.now();
            }
    }
}



