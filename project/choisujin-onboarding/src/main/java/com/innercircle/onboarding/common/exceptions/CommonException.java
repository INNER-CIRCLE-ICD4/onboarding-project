package com.innercircle.onboarding.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.innercircle.onboarding.common.response.ResponseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonException extends RuntimeException {

    private ResponseStatus response;
    private String description;
    private Object details;

    public CommonException(ResponseStatus response) {
        this.response = response;
    }

    public CommonException(ResponseStatus response, String description) {
        this.response = response;
    }

    @Builder
    public CommonException(ResponseStatus response, String description, Object details) {
        this.response = response;
        this.description = description;
        this.details = details;
    }

}
