package fastcampus.onboarding.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public static ApiResponse success(HttpStatus status, String message) {
        return ApiResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse error(HttpStatus status, String message) {
        return ApiResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}