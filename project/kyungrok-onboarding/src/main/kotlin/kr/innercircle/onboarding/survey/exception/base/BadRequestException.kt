package kr.innercircle.onboarding.survey.exception.base

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : BadRequestException
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

open class BadRequestException(val errorCode: ErrorCode): RuntimeException()
