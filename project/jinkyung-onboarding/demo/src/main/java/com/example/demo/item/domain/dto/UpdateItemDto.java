package com.example.demo.item.domain.dto;

import com.example.demo.utill.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemDto {

    private int itemNo;

    private String itemName;

    private String itemDescription;


    private ItemType itemType;

    private List<UpdateItemQuestionDto> itemQuestionList;

    private boolean isRequired;

}
