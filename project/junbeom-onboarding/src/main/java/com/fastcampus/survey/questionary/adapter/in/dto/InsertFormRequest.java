package com.fastcampus.survey.questionary.adapter.in.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertFormRequest {

    private String name;
    private String describe;
    private List<InsertContentRequest> contents;


}