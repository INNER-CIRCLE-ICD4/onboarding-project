// ChoiceUpdateRequest.java
package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChoiceUpdateRequest {
    private UUID id;    // 기존 선택지 수정 시 필요
    private String text;
    private int choiceIndex;
}
