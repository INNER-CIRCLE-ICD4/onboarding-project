package kr.innercircle.onboarding.survey.exception.base

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : NotFoundException
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

class NotFoundException(val errorCode: ErrorCode): RuntimeException()