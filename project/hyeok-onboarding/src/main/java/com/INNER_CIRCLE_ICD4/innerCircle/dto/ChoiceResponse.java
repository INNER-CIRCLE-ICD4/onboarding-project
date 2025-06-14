package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.UUID;

public record ChoiceResponse(
        UUID id,
        String text,
        int choiceIndex
) {}
