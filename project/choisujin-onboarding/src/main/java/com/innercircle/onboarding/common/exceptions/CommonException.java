package com.innercircle.onboarding.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.innercircle.onboarding.common.response.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonException extends RuntimeException {

    private ResponseStatus response;
    private String description;

    public CommonException(ResponseStatus response) {
        this.response = response;
    }

    public CommonException(ResponseStatus response, String description) {
        this.response = response;
        this.description = description;
    }

}
