package com.multi.sungwoongonboarding.options.dto;

import com.multi.sungwoongonboarding.options.domain.Options;

public record OptionCreateResponse(Long id, String optionText, int order) {
    public static OptionCreateResponse fromDomain(Options option) {
        return new OptionCreateResponse(option.getId(), option.getOptionText(), option.getOrder());
    }
}
