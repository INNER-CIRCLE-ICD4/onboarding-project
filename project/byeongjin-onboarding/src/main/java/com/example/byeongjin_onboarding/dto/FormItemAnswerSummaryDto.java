package com.example.byeongjin_onboarding.dto;

import com.example.byeongjin_onboarding.entity.ItemType; // 설문 항목 유형을 위한 Enum (기존에 정의되어 있을 것으로 가정)
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // 기본 생성자 필요

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class FormItemAnswerSummaryDto {
    private Long formItemId;
    private String formItemName;
    private String formItemDescription;
    private ItemType itemType;
    private boolean required;
    private int displayOrder;

    private Map<String, Long> optionCounts;

    private List<String> textAnswers;
}