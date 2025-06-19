package fc.icd.jaehyeononboarding.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum InputType {
    TEXT,
    LONG_TEXT,
    RADIO,
    CHECKBOX;

    @JsonCreator
    public static InputType from(String input) {
        return InputType.valueOf(input.toUpperCase());
    }
}
