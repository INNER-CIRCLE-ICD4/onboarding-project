package com.multi.sungwoongonboarding.options.dto;

import com.multi.sungwoongonboarding.options.domain.Options;

public record OptionResponse(Long id,
                             String optionText,
                             Boolean deleted) {
    public static OptionResponse fromDomain(Options option) {
        return new OptionResponse(option.getId(), option.getOptionText(), option.getDeleted());
    }
}
