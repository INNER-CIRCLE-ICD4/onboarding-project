package fc.icd.baulonboarding.common.reponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다.");

    private final String errorMsg;


}
