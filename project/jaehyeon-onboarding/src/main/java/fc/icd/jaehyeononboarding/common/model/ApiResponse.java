package fc.icd.jaehyeononboarding.common.model;

import fc.icd.jaehyeononboarding.common.constants.ResultCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public ApiResponse(ResultCodes codes, T data) {
        this.code = codes.getCode();
        this.message = codes.getMessage();
        this.data = data;
    }
}
