package fc.icd.jaehyeononboarding.common.constants;

import lombok.Getter;

@Getter
public enum ResultCodes {

    RC_10000(10000, "성공");

    private final int code;
    private final String message;

    ResultCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
