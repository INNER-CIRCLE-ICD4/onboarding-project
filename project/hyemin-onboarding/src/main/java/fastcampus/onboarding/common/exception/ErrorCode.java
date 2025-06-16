package fastcampus.onboarding.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 클라이언트 에러 (400번대)
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다"),
    FORM_NOT_FOUND(400, "C003", "설문지를 찾을 수 없습니다"),
    ITEM_NOT_FOUND(400, "C004", "설문항목을 찾을 수 없습니다"),
    OPTION_NOT_FOUND(400, "C005", "항목옵션을 찾을 수 없습니다"),
    DUPLICATE_ENTITY(400, "C006", "이미 존재하는 데이터입니다"),

    // 서버 에러 (500번대)
    INTERNAL_SERVER_ERROR(500, "S001", "서버 오류가 발생했습니다");

    private final int status;
    private final String code;
    private final String message;
}
