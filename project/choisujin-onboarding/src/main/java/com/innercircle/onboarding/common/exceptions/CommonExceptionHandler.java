package com.innercircle.onboarding.common.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.innercircle.onboarding.common.response.ApiResponse;
import com.innercircle.onboarding.common.response.ResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * 공통 예외 처리
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse.Exception> commonExceptionHandler(HttpServletRequest request, CommonException exception) {
        return ResponseEntity
                .status(exception.getResponse().getHttpsStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.Exception
                                .builder()
                                .path(request.getServletPath())
                                .commonException(exception)
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        ArrayNode descriptions = new ObjectMapper().createArrayNode();
        fieldErrors.forEach(e -> {
            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("field", e.getField());
            node.put("message", e.getDefaultMessage());
            descriptions.add(node);
        });

        return ResponseEntity
                .status(exception.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.Exception
                                .builder()
                                .path(request.getServletPath())
                                .commonException(
                                        CommonException.builder()
                                                .response(ResponseStatus.VALIDATION_FAIL)
                                                .details(descriptions)
                                                .build()
                                )
                                .build()
                );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> notSupportedMethodExceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {

        return ResponseEntity
                .status(exception.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.Exception
                                .builder()
                                .path(request.getServletPath())
                                .commonException(
                                        CommonException.builder()
                                                .response(ResponseStatus.METHOD_NOT_SUPPORT)
                                                .description("Method Not Allowed. Please check the request method.")
                                                .build()
                                )
                                .build()
                );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> notSupportedMethodExceptionHandler(HttpServletRequest request, NoResourceFoundException exception) {

        return ResponseEntity
                .status(exception.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.Exception
                                .builder()
                                .path(request.getServletPath())
                                .commonException(
                                        CommonException.builder()
                                                .response(ResponseStatus.NOT_FOUND_URL)
                                                .description("URL Not Found. Please check the request Api url.")
                                                .build()
                                )
                                .build()
                );
    }

}
