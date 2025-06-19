package com.multi.sungwoongonboarding.options.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionUpdateRequest {

    private final Long id;
    @NotBlank(message = "옵션 내용은 필수 입력 항목입니다.")
    private final String optionText;
    @NotBlank(message = "순서를 입력해주세요.")
    private final int order;
    @NotBlank(message = "삭제 여부를 입력해주세요.")
    private final Boolean deleted;
}
