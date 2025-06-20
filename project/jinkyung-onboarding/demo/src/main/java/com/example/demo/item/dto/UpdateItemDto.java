package com.example.demo.item.dto;

import com.example.demo.item.Item;
import com.example.demo.itemQuestion.ItemQuestion;
import com.example.demo.utill.ItemType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

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
