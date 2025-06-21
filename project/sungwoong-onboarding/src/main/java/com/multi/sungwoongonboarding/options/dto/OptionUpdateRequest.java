package com.multi.sungwoongonboarding.options.dto;

import com.multi.sungwoongonboarding.options.domain.Options;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionUpdateRequest {

    @NotBlank(message = "옵션 내용은 필수 입력 항목입니다.")
    private String optionText;

    public Options toDomain() {
        return Options.builder()
                .optionText(this.optionText)
                .build();

    }
}
