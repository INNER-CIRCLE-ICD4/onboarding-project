package fc.innercircle.sanghyukonboarding.common.domain.exception

class CustomException(
    val formattedErrorCode: ErrorCode.FormattedErrorCode,
    e: Throwable? = null,
) : RuntimeException(formattedErrorCode.message, e) {
    // ErrorCode만 받을 때 사용하는 생성자
    constructor(errorCode: ErrorCode) : this(errorCode.withArgs(), null)

    // ErrorCode와 원인 예외를 함께 받을 때 사용하는 생성자
    constructor(errorCode: ErrorCode, cause: Throwable?) : this(errorCode.withArgs(), cause)
}
