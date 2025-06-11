package com.example.demo.item.dto;

import com.example.demo.utill.ItemType;
import com.example.demo.item.Item;
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

    @NotNull
    private String itemDescription;

    @NotNull
    private ItemType itemType;

    private String itemValue;

    private List<String> itemQuestionList;

    @NotNull
    private boolean isRequired;

    public Item toEntity(){
        return Item.builder()
                .itemName(this.itemName)
                .itemDescription(this.itemDescription)
                .itemType(this.itemType)
                .itemValue(itemValue)
                .itemQuestion(
                        this.itemType.equals(ItemType.singleChoice) || this.itemType.equals(ItemType.multipleChoice)
                                ?  itemQuestionList.stream()
                                    .map(
                                        Item.ItemQuestion::toEntity
                                    ).collect(Collectors.toList())
                                : null
                )
                .isRequired(this.isRequired)
                .build();
    }



}
