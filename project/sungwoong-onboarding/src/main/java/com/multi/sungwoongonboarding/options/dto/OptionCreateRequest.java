package com.multi.sungwoongonboarding.options.dto;

import com.multi.sungwoongonboarding.options.domain.Options;
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

    public Options toDomain() {
        return Options.builder()
                .optionText(this.optionText)
                .order(this.order)
                .build();

    }

}
