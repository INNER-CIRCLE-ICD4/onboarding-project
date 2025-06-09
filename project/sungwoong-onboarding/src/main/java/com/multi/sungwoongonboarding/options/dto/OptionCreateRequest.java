package com.multi.sungwoongonboarding.options.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionCreateRequest {

    @NotBlank(message = "옵션 내용은 필수 입력 항목입니다.")
    private final String optionText;
    private final int order;

}
