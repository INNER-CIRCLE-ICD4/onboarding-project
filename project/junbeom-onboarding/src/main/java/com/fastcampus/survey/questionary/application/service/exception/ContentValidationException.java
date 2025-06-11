package com.fastcampus.survey.questionary.application.service.exception;

import jakarta.validation.ValidationException;

public class ContentValidationException extends ValidationException {
    public ContentValidationException(String field, String message) {
        super(String.format("%s: %s", field, message));
    }
}
