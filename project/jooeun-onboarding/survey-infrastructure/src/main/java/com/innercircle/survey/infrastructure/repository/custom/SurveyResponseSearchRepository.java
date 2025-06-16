package com.innercircle.survey.infrastructure.repository.custom;

import com.innercircle.survey.domain.response.SurveyResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 응답 고급 검색을 위한 커스텀 Repository 인터페이스
 */
public interface SurveyResponseSearchRepository {

    /**
     * 고급 검색 조건으로 응답 검색
     *
     * @param surveyId 설문조사 ID
     * @param questionKeyword 질문 제목 키워드
     * @param answerKeyword 응답 값 키워드
     * @param respondentKeyword 응답자 정보 키워드
     * @param startDate 검색 시작일
     * @param endDate 검색 종료일
     * @return 검색 조건에 맞는 응답 목록
     */
    List<SurveyResponse> searchResponses(String surveyId,
                                        String questionKeyword,
                                        String answerKeyword,
                                        String respondentKeyword,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate);

    /**
     * 고급 검색 조건으로 응답 개수 조회
     *
     * @param surveyId 설문조사 ID
     * @param questionKeyword 질문 제목 키워드
     * @param answerKeyword 응답 값 키워드
     * @param respondentKeyword 응답자 정보 키워드
     * @param startDate 검색 시작일
     * @param endDate 검색 종료일
     * @return 검색 조건에 맞는 응답 개수
     */
    long countSearchResponses(String surveyId,
                             String questionKeyword,
                             String answerKeyword,
                             String respondentKeyword,
                             LocalDateTime startDate,
                             LocalDateTime endDate);

    /**
     * 설문조사 통계 데이터 조회
     *
     * @param surveyId 설문조사 ID
     * @return 통계 계산에 필요한 모든 응답 데이터
     */
    List<SurveyResponse> findAllResponsesForStatistics(String surveyId);

    /**
     * 최근 N일간의 일별 응답 수 조회
     *
     * @param surveyId 설문조사 ID
     * @param days 조회할 일수
     * @return 일별 응답 수 맵 (날짜 -> 응답 수)
     */
    List<Object[]> getDailyResponseCounts(String surveyId, int days);
}
