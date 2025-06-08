package com.multi.sungwoongonboarding.forms.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Forms {
    private final Long id;
    private final String title;
    private final String description;
}
