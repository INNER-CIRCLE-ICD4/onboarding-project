package kr.innercircle.onboarding.survey.exception

import kr.innercircle.onboarding.survey.exception.base.BadRequestException
import kr.innercircle.onboarding.survey.exception.base.ErrorCode

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : InsufficientSurveyItemOptionsException
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */
class InsufficientSurveyItemOptionsException: BadRequestException(ErrorCode.INSUFFICIENT_SURVEY_ITEM_OPTIONS)