package fc.innercircle.jinhoonboarding.common.exception

abstract class CommonException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
): RuntimeException(message)