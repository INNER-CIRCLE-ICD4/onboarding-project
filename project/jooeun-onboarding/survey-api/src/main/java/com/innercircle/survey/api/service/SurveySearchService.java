package com.innercircle.survey.api.service;

import com.innercircle.survey.api.dto.response.SurveyResponseSearchResult;
import com.innercircle.survey.api.exception.SurveyNotFoundException;
import com.innercircle.survey.domain.response.SurveyAnswer;
import com.innercircle.survey.domain.response.SurveyResponse;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.infrastructure.repository.SurveyRepository;
import com.innercircle.survey.infrastructure.repository.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 설문조사 응답 검색 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveySearchService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    /**
     * 설문조사 응답 고급 검색
     *
     * @param surveyId 설문조사 ID
     * @param questionKeyword 질문 제목 키워드
     * @param answerKeyword 응답 값 키워드
     * @param respondentKeyword 응답자 정보 키워드
     * @param startDate 검색 시작일
     * @param endDate 검색 종료일
     * @return 검색 결과
     */
    public SurveyResponseSearchResult searchResponses(String surveyId,
                                                      String questionKeyword,
                                                      String answerKeyword,
                                                      String respondentKeyword,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate) {

        log.info("설문조사 응답 검색 시작 - surveyId: {}, questionKeyword: {}, answerKeyword: {}, " +
                        "respondentKeyword: {}, startDate: {}, endDate: {}",
                surveyId, questionKeyword, answerKeyword, respondentKeyword, startDate, endDate);

        // 설문조사 존재 확인
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException("설문조사를 찾을 수 없습니다: " + surveyId));

        // 검색 실행
        List<SurveyResponse> searchResults = surveyResponseRepository.searchResponses(
                surveyId, questionKeyword, answerKeyword, respondentKeyword, startDate, endDate);

        long totalCount = surveyResponseRepository.countSearchResponses(
                surveyId, questionKeyword, answerKeyword, respondentKeyword, startDate, endDate);

        // 검색 조건 생성
        SurveyResponseSearchResult.SearchCondition searchCondition =
                new SurveyResponseSearchResult.SearchCondition(
                        surveyId, questionKeyword, answerKeyword, respondentKeyword, startDate, endDate);

        // 응답 요약 생성
        List<SurveyResponseSearchResult.SurveyResponseSummary> responseSummaries =
                searchResults.stream()
                        .map(response -> createResponseSummary(response, questionKeyword, answerKeyword))
                        .collect(Collectors.toList());

        log.info("설문조사 응답 검색 완료 - 총 {}건 검색됨", totalCount);

        return new SurveyResponseSearchResult(
                totalCount,
                searchCondition,
                responseSummaries,
                LocalDateTime.now()
        );
    }

    /**
     * 응답 요약 정보 생성
     */
    private SurveyResponseSearchResult.SurveyResponseSummary createResponseSummary(
            SurveyResponse response, String questionKeyword, String answerKeyword) {

        // 매칭된 응답들 찾기
        List<SurveyResponseSearchResult.MatchedAnswer> matchedAnswers =
                response.getAnswers().stream()
                        .filter(answer -> isAnswerMatched(answer, questionKeyword, answerKeyword))
                        .map(answer -> createMatchedAnswer(answer, questionKeyword, answerKeyword))
                        .collect(Collectors.toList());

        return new SurveyResponseSearchResult.SurveyResponseSummary(
                response.getId(),
                response.getRespondentInfo(),
                response.getCreatedAt(),
                response.getAnsweredQuestionCount(),
                matchedAnswers
        );
    }

    /**
     * 응답이 검색 조건에 매칭되는지 확인
     */
    private boolean isAnswerMatched(SurveyAnswer answer, String questionKeyword, String answerKeyword) {
        boolean questionMatch = isNullOrEmpty(questionKeyword) ||
                answer.questionTitleContains(questionKeyword);

        boolean answerMatch = isNullOrEmpty(answerKeyword) ||
                answer.containsValue(answerKeyword);

        return questionMatch || answerMatch;
    }

    /**
     * 매칭된 응답 정보 생성
     */
    private SurveyResponseSearchResult.MatchedAnswer createMatchedAnswer(
            SurveyAnswer answer, String questionKeyword, String answerKeyword) {

        List<String> matchReasons = new ArrayList<>();

        if (!isNullOrEmpty(questionKeyword) && answer.questionTitleContains(questionKeyword)) {
            matchReasons.add("QUESTION_TITLE_MATCH");
        }

        if (!isNullOrEmpty(answerKeyword) && answer.containsValue(answerKeyword)) {
            matchReasons.add("ANSWER_VALUE_MATCH");
        }

        return new SurveyResponseSearchResult.MatchedAnswer(
                answer.getQuestionId(),
                answer.getQuestionTitle(),
                answer.getAllAnswers(),
                matchReasons
        );
    }

    /**
     * 문자열이 null이거나 빈 값인지 확인
     */
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
