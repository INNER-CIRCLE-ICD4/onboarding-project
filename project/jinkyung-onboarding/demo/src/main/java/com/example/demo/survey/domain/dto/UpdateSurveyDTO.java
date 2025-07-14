package com.example.demo.survey.domain.dto;

import com.example.demo.item.domain.Item;
import com.example.demo.item.domain.dto.UpdateItemDto;
import com.example.demo.survey.domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSurveyDTO {

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
