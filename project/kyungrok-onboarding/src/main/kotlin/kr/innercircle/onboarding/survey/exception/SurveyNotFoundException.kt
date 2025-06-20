package kr.innercircle.onboarding.survey.exception

import kr.innercircle.onboarding.survey.exception.base.BadRequestException
import kr.innercircle.onboarding.survey.exception.base.ErrorCode

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : SurveyNotFoundException
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
class SurveyNotFoundException: BadRequestException(ErrorCode.SURVEY_NOT_FOUND)