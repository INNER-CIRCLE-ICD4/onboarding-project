package fc.innercircle.sanghyukonboarding.common.domain.exception

class CustomException(
    val errorCode: ErrorCode.FormattedErrorCode,
) : RuntimeException(errorCode.message)
