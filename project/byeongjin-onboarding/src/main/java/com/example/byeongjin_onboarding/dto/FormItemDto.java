package com.example.byeongjin_onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormItemDto {
    private Long id;
    private String name;
    private String description;
    private String itemType;
    private boolean required;
    private int displayOrder;
    private List<String> options;
}