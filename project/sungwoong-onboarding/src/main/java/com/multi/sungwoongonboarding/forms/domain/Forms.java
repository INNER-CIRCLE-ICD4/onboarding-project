package com.multi.sungwoongonboarding.forms.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class Forms {
    private final Long id;
    private final String title;
    private final String description;
}
