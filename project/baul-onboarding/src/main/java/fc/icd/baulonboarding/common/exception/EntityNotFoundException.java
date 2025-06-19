package fc.icd.baulonboarding.common.exception;

import fc.icd.baulonboarding.common.reponse.ErrorCode;

public class EntityNotFoundException extends BaseException{

    public EntityNotFoundException() {
        super(ErrorCode.COMMON_ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.COMMON_ENTITY_NOT_FOUND);
    }

}
