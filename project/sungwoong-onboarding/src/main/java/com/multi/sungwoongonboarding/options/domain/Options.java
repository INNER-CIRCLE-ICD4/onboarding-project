package com.multi.sungwoongonboarding.options.domain;

import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Options {
    private final Long id;
    private final String optionText;
    private final int order;
}
