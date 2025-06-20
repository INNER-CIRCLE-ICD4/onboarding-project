package kr.innercircle.onboarding.survey.exception

import kr.innercircle.onboarding.survey.exception.base.BadRequestException
import kr.innercircle.onboarding.survey.exception.base.ErrorCode

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : InvalidSurveyItemOptionAnswer
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
class InvalidSurveyItemOptionAnswer: BadRequestException(ErrorCode.INVALID_SURVEY_ITEM_OPTION_ANSWER)