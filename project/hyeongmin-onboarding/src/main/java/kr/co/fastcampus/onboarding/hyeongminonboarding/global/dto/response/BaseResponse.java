package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * 공통 응답 클래스
 *
 */
@Builder
@AllArgsConstructor
@Data
public class BaseResponse<T> {
    public static final int BASE_SUCCESS_STATUS = 200;
    public static final String BASE_SUCCESS_CODE = "SUCCESS";


    private String timestamp;
    private int status;
    private String code;
    private String message;
    private String path;
    private T data;

    protected BaseResponse(int status, String code, String message, T data, String path) {
        this.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.path = path;
    }

    public BaseResponse() {
        this.timestamp =  DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

    public static <T> BaseResponse<T> OK() {
        return new BaseResponse<>(BASE_SUCCESS_STATUS, BASE_SUCCESS_CODE, null, null, null);
    }

    public static <T> BaseResponse<T> OK(String path) {
        return new BaseResponse<>(BASE_SUCCESS_STATUS, BASE_SUCCESS_CODE, null, null, path);
    }

    public static <T> BaseResponse<T> OK(T data) {
        return new BaseResponse<>(BASE_SUCCESS_STATUS, BASE_SUCCESS_CODE, null, data, null);
    }

    public static <T> BaseResponse<T> OK(T data, String path) {
        return new BaseResponse<>(BASE_SUCCESS_STATUS, BASE_SUCCESS_CODE, null, data, path);
    }

//    public static <T> BaseResponse<T> ERROR(String message) {
//        BaseResponse response = new BaseResponse();
//        response.setCode(BASE_ERROR_CODE);
//        response.setMessage(message);
//        response.setResponseTime(LocalDateTime.now());
//
//        return response;
//    }
//
//    public static <T> BaseResponse<T> ERROR(Exception e) {
//        BaseResponse response = new BaseResponse();
//        response.setCode(BASE_ERROR_CODE);
//        response.setMessage(e.getMessage());
//        response.setResponseTime(LocalDateTime.now());
//        return response;
//    }
//
//
//    public static <T> BaseResponse<T> ERROR(MessageSource messageSource, ErrorInfo errorInfo) {
//        BaseResponse response = BaseResponse.builder()
//                .code(errorInfo.getCode())
//                .message(messageSource.getMessage(errorInfo.getMessageCode(), null, LocaleContextHolder.getLocale()))
//                .systemMessage(errorInfo.getMessage())
//                .responseTime(LocalDateTime.now())
//                .build();
//        return response;
//    }
//
//    public static <T> BaseResponse<T> ERROR(MessageSource messageSource, ErrorInfo errorInfo, String errorMsg) {
//        BaseResponse response = BaseResponse.builder()
//                .code(errorInfo.getCode())
//                .message(messageSource.getMessage(errorInfo.getMessageCode(), null, LocaleContextHolder.getLocale()))
//                .systemMessage(errorMsg)
//                .responseTime(LocalDateTime.now())
//                .build();
//        return response;
//    }
//
//    public static <T> BaseResponse<T> ERROR(Integer code, String message, String systemMessage) {
//        return new BaseResponse(code, message, systemMessage);
//    }

}
