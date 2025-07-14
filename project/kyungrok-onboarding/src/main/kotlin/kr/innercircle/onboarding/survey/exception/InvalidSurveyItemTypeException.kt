package kr.innercircle.onboarding.survey.exception

import kr.innercircle.onboarding.survey.exception.base.BadRequestException
import kr.innercircle.onboarding.survey.exception.base.ErrorCode


/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : InvalidSurveyItemTypeException
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */

class InvalidSurveyItemTypeException: BadRequestException(ErrorCode.INVALID_SURVEY_ITEM_INPUT_TYPE)