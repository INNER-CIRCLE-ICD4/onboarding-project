package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitSurveyAnswersRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyUpdateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;

public interface ISurveyService {

    /**
     * 설문조사를 생성합니다.
     * 요청에는 설문 제목, 설명, 질문 목록 및 질문별 옵션 정보가 포함됩니다.
     *
     * @param request 설문조사 생성 요청 정보
     * @return 생성된 설문조사의 상세 정보
     */
    SurveyResponseDto createSurvey(BaseRequest<SurveyCreateRequest> request);

    /**
     * 기존 설문조사를 수정합니다.
     * 질문 추가/삭제/수정이 가능하며, 기존 응답은 영향을 받지 않습니다.
     *
     * @param surveyId 수정 대상 설문조사 ID
     * @param request 설문조사 수정 요청 정보
     * @return 수정된 설문조사의 상세 정보
     */
    SurveyResponseDto updateSurvey(Long surveyId, BaseRequest<SurveyUpdateRequest> request);

    /**
     * 설문조사에 대한 응답을 제출합니다.
     * 질문 타입에 따라 텍스트 또는 옵션 ID를 제출하며,
     * 유효성 검사를 수행합니다.
     *
     * @param surveyId 응답할 설문조사 ID
     * @param request 응답 제출 요청 정보
     */
    void submitSurveyAnswer(Long surveyId, BaseRequest<SubmitSurveyAnswersRequest> request);

    /**
     * 설문조사에 제출된 전체 응답을 조회합니다.
     * 응답자 목록과 각 응답 항목의 정보가 포함됩니다.
     *
     * @param surveyId 조회할 설문조사 ID
     * @return 응답이 포함된 설문조사 상세 정보
     */
    SurveyWithAnswersResponseDto getSurveyResponses(Long surveyId);
}
