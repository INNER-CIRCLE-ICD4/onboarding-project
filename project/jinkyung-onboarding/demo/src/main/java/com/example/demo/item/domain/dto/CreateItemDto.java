package com.example.demo.item.domain.dto;

import com.example.demo.item.domain.Item;
import com.example.demo.item.domain.ItemQuestion;
import com.example.demo.utill.ItemType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {

    @NotNull
    private String itemName;

    private String itemDescription;

    @NotNull
    private ItemType itemType;

    private List<String> itemQuestionList;

    @NotNull
    private boolean isRequired;

    public Item toEntity(){
        return Item.builder()
                .itemName(this.itemName)
                .itemDescription(this.itemDescription)
                .itemType(this.itemType)
                .itemQuestion(
                        this.itemType.equals(ItemType.SingleChoice) || this.itemType.equals(ItemType.MultipleChoice)
                                ?  itemQuestionList.stream()
                                    .map(
                                        ItemQuestion::toEntity
                                    ).collect(Collectors.toList())
                                : null
                )
                .isRequired(this.isRequired)
                .build();
    }



}
