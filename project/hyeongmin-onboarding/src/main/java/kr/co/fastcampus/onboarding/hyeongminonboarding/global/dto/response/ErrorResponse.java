package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;        // HTTP status code
    private String error;      // HTTP status name
    private String code;       // 서비스 내 비즈니스 에러 코드
    private String message;    // 사용자에게 보여줄 메시지
    private String systemMessage;    // 사용자에게 보여줄 메시지
    private String path;       // 요청 URI

    private List<FieldError> fieldErrors;

    @Data
    @AllArgsConstructor
    public static class FieldError {
        private String field;          // 문제가 된 필드
        private Object rejectedValue;  // 클라이언트가 보낸 값
        private String reason;         // 실패 사유
    }
}