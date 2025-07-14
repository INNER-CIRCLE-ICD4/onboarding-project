package com.example.demo.answer.domain.dto;

import com.example.demo.answer.domain.ItemAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemAnswerDto {


    private int itemNo;

    private String itemValue;

    private boolean isDelete;

    public ItemAnswer toEntity() {
        return ItemAnswer.builder()
                .itemNo(this.itemNo)
                .itemValue(this.itemValue)
                .build();
    }

}
