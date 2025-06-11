package fastcampus.onboarding.common.exception;

public enum ErrorCode {
    // 공통 에러
    ENTITY_NOT_FOUND(404, "C001", "데이터를 찾을 수 없습니다"),
    DUPLICATE_RESOURCE(409, "C002", "중복된 리소스가 존재합니다"),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류가 발생했습니다"),
    INVALID_TYPE_VALUE(400, "C004", "유효하지 않은 타입입니다"),
    
    // 사용자 관련 에러
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다"),
    EMAIL_DUPLICATION(400, "U002", "이미 등록된 이메일입니다"),
    INVALID_INPUT_VALUE(400, "U003", "유효하지 않은 입력값입니다");
    
    private final int status;
    private final String code;
    private final String message;
    
    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
