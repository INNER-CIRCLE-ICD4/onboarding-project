package com.example.demo.utill;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiResponseTemplate<T> {

    private final HttpStatus status;

    private final boolean success;

    private final T data;



    public ApiResponseTemplate(T data) {
        this.status = HttpStatus.OK;
        this.success = true;
        this.data = data;
    }

    public ApiResponseTemplate(T data,HttpStatus status) {
        this.status = status;
        this.success = true;
        this.data = data;
    }
}
