package fc.innercircle.jinhoonboarding.survey.exception

import fc.innercircle.jinhoonboarding.common.exception.CommonException
import fc.innercircle.jinhoonboarding.common.exception.ErrorCode


class SurveyException(errorCode: ErrorCode) : CommonException(errorCode)