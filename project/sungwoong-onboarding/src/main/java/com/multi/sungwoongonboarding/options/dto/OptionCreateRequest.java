package com.multi.sungwoongonboarding.options.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionCreateRequest {

    private final String optionText;
    private final int order;

}
