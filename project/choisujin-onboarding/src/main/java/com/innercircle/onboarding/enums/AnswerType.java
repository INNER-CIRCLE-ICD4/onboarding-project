package com.innercircle.onboarding.enums;

import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.response.ResponseStatus;
import lombok.Getter;

@Getter
public enum AnswerType {
    SHORT_TEXT,
    LONG_TEXT,
    SINGLE_CHOICE,
    MULTIPLE_CHOICE;

    public static boolean checkValid(String type) {
        for(AnswerType enums : AnswerType.values()) {
            if (enums.name().equals(type)) {
                return true;
            }
        }
        throw new CommonException(ResponseStatus.VALIDATION_FAIL, "[AnswerType] is not valid: " + type);
    }


}
