package fc.icd.jaehyeononboarding.common.exception;

import fc.icd.jaehyeononboarding.common.constants.ResultCodes;

public class ApiCommonException extends RuntimeException implements ResultCodeProvider {

    private final ResultCodes code;

    public ApiCommonException(ResultCodes code) {
        this.code = code;
    }

    @Override
    public ResultCodes getCode() {
        return this.code;
    }
}
