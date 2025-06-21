package com.multi.sungwoongonboarding.submission.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SelectedOption {
    private Long optionId;
    private String optionText;



    public static SelectedOption ofOptionId(Long optionId) {
        return SelectedOption.builder()
                .optionId(optionId)
                .build();
    }
}
