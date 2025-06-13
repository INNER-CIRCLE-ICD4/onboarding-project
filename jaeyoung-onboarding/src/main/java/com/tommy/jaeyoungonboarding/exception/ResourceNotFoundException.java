package com.tommy.jaeyoungonboarding.exception;

public class ResourceNotFoundException extends RuntimeException{

    // 단일 Exception 추적
    public ResourceNotFoundException(String message){
        super(message);
    }

    // ResouceNotFoundException 포함 다수 Exception 추적
    public ResourceNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
