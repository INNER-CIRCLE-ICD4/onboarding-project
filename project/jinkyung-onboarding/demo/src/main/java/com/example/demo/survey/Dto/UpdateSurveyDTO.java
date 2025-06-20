package com.example.demo.survey.Dto;

import com.example.demo.item.Item;
import com.example.demo.item.dto.CreateItemDto;
import com.example.demo.item.dto.UpdateItemDto;
import com.example.demo.survey.Survey;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSurveyDTO {

    @NotNull
    private Long surveyId;

    private String name;


    private String description;


    private List<UpdateItemDto> items;

    public Survey toEntity(List<Item> items){
        return Survey.builder()
                .name(this.name)
                .description(this.description)
                .items(items)
                .build();

    }

}
