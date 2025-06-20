package kr.innercircle.onboarding.survey.exception

import kr.innercircle.onboarding.survey.exception.base.BadRequestException
import kr.innercircle.onboarding.survey.exception.base.ErrorCode

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : SurveyItemNotFoundException
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
class SurveyItemNotFoundException: BadRequestException(ErrorCode.SURVEY_ITEM_NOT_FOUND) {
}