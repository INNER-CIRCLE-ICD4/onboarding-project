package fc.icd.jaehyeononboarding.common.constants;

import lombok.Getter;

@Getter
public enum ResultCodes {

    RC_10000(10000, "성공"),
    RC_20010(20010, "요청 자원 없음"),
    RC_30000(30000, "내부 오류")
    ;

    private final int code;
    private final String message;

    ResultCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
