package com.onboarding.api.dto.response;

import com.onboarding.model.survey.Option;
import lombok.Data;

@Data
public class OptionsRes {
    private String optionId;
    private String label;

    public OptionsRes(String id, String text) {
        this.optionId = id;
        this.label = text;
    }

    public static OptionsRes of(Option option) {
        return new OptionsRes(option.getId(), option.getText());
    }
}
