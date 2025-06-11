package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyCreateResponse;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;

public interface ISurveyService {

    /**
     * 설문조사 생성
     * @param request
     * @return
     */
    SurveyCreateResponse createSurvey(BaseRequest<SurveyCreateRequest> request);
}
