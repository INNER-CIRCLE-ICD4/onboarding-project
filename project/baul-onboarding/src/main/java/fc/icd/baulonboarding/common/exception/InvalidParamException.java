package fc.icd.baulonboarding.common.exception;

import fc.icd.baulonboarding.common.reponse.ErrorCode;

public class InvalidParamException extends BaseException{

    public InvalidParamException() {
        super(ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public InvalidParamException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidParamException(String errorMsg) {
        super(errorMsg, ErrorCode.COMMON_INVALID_PARAMETER);
    }


}
