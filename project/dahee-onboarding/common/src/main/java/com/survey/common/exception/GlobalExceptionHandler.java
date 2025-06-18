package com.survey.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외 처리
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleAppEx(ApplicationException ex) {
        ErrorCode code = ex.getErrorCode();
        ApiErrorResponse res = new ApiErrorResponse(code.name(), ex.getMessage());
        return ResponseEntity.status(code.getStatus()).body(res);
    }

    // 그 외 예상 못한 예외(개발중 NullPointer 등)도 한 번에 잡을 수 있음
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknown(Exception ex) {
        ApiErrorResponse res = new ApiErrorResponse(
                ErrorCode.INTERNAL_ERROR.name(),
                "알 수 없는 오류가 발생했습니다."
        );
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(res);
    }
}
