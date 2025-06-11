package com.innercircle.onboarding.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.innercircle.onboarding.common.exceptions.CommonException;
import lombok.Builder;
import lombok.Getter;

public class ApiResponse {

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Base<T> {

        private int status;
        private String code;
        private String message;
        private T data;

        public Base(T data) {
            this.status = ResponseStatus.SUCCESS.getHttpsStatus();
            this.code = ResponseStatus.SUCCESS.getCode();
            this.message = ResponseStatus.SUCCESS.getMessage();
            this.data = data;
        }

        public static <T> Base<T> success() {
            return new Base<>(null);
        }

        public static <T> Base<T> success(T data) {
            return new Base<>(data);
        }

    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Exception {

        private int status;
        private String code;
        private String message;
        private String path;
        private String description;

        @Builder
        public Exception(String path, CommonException commonException) {
            this.status = commonException.getResponse().getHttpsStatus();
            this.code = commonException.getResponse().getCode();
            this.message = commonException.getResponse().getMessage();
            this.path = path;
            this.description = commonException.getDescription();
        }

    }

}
