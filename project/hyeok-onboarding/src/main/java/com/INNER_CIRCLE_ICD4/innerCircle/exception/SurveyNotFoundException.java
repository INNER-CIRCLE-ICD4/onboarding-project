// src/main/java/com/INNER_CIRCLE_ICD4/innerCircle/exception/SurveyNotFoundException.java
package com.INNER_CIRCLE_ICD4.innerCircle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(String message) {
        super(message);
    }
}
