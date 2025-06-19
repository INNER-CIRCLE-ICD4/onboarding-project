package fc.icd.jaehyeononboarding.common.model;

import fc.icd.jaehyeononboarding.common.constants.ResultCodes;
import lombok.Getter;

@Getter
public class NoDataResponse extends ApiResponse<Void> {

    public NoDataResponse() {
        this(ResultCodes.RC_10000);
    }

    public NoDataResponse(ResultCodes result) {
        super(result, null);
    }
}
