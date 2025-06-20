package com.example.demo.item.dto;

import com.example.demo.item.Item;
import com.example.demo.itemQuestion.ItemQuestion;
import com.example.demo.utill.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemQuestionDto {

    private int questionNo;

    private String question;

}
