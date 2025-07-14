package com.example.demo.survey.domain.dto;

import com.example.demo.item.domain.Item;
import com.example.demo.item.domain.dto.CreateItemDto;
import com.example.demo.survey.domain.Survey;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSurveyDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private List<CreateItemDto> items = new ArrayList<>();

    public Survey toEntity(List<Item> items){
        return Survey.builder()
                .name(this.name)
                .description(this.description)
                .items(items)
                .build();

    }

}
