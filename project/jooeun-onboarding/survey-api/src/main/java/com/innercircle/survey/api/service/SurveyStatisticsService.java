package com.innercircle.survey.api.service;

import com.innercircle.survey.api.dto.response.SurveyStatisticsResult;
import com.innercircle.survey.api.exception.SurveyNotFoundException;
import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.response.SurveyAnswer;
import com.innercircle.survey.domain.response.SurveyResponse;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import com.innercircle.survey.infrastructure.repository.SurveyRepository;
import com.innercircle.survey.infrastructure.repository.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 설문조사 통계 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyStatisticsService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    /**
     * 설문조사 통계 조회 (캐시 적용)
     *
     * @param surveyId 설문조사 ID
     * @return 통계 결과
     */
    @Cacheable(value = "survey-statistics", key = "#surveyId", unless = "#result == null")
    public SurveyStatisticsResult getSurveyStatistics(String surveyId) {
        log.info("설문조사 통계 계산 시작 - surveyId: {}", surveyId);

        // 설문조사 조회
        Survey survey = surveyRepository.findByIdWithActiveQuestions(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException("설문조사를 찾을 수 없습니다: " + surveyId));

        // 통계용 응답 데이터 조회
        List<SurveyResponse> responses = surveyResponseRepository.findAllResponsesForStatistics(surveyId);
        long totalResponseCount = responses.size();

        log.info("통계 계산 대상 응답 수: {}", totalResponseCount);

        // 질문별 통계 계산
        List<SurveyStatisticsResult.QuestionStatistics> questionStatistics = 
                calculateQuestionStatistics(survey.getActiveQuestions(), responses, totalResponseCount);

        // 응답 트렌드 계산
        SurveyStatisticsResult.ResponseTrend responseTrend = 
                calculateResponseTrend(surveyId);

        SurveyStatisticsResult result = SurveyStatisticsResult.builder()
                .surveyId(surveyId)
                .surveyTitle(survey.getTitle())
                .totalResponseCount(totalResponseCount)
                .calculatedAt(LocalDateTime.now())
                .questionStatistics(questionStatistics)
                .responseTrend(responseTrend)
                .build();

        log.info("설문조사 통계 계산 완료 - surveyId: {}, 총 응답 수: {}", surveyId, totalResponseCount);

        return result;
    }

    /**
     * 질문별 통계 계산
     */
    private List<SurveyStatisticsResult.QuestionStatistics> calculateQuestionStatistics(
            List<SurveyQuestion> questions, List<SurveyResponse> responses, long totalResponseCount) {
        
        return questions.stream()
                .map(question -> calculateQuestionStatistic(question, responses, totalResponseCount))
                .collect(Collectors.toList());
    }

    /**
     * 개별 질문 통계 계산
     */
    private SurveyStatisticsResult.QuestionStatistics calculateQuestionStatistic(
            SurveyQuestion question, List<SurveyResponse> responses, long totalResponseCount) {
        
        // 이 질문에 대한 모든 응답 수집
        List<SurveyAnswer> answersForQuestion = responses.stream()
                .flatMap(response -> response.getAnswers().stream())
                .filter(answer -> question.getId().equals(answer.getQuestionId()))
                .collect(Collectors.toList());

        long responseCount = answersForQuestion.size();
        double responseRate = totalResponseCount > 0 ? 
                (double) responseCount / totalResponseCount * 100 : 0.0;

        SurveyStatisticsResult.QuestionStatistics.QuestionStatisticsBuilder builder = 
                SurveyStatisticsResult.QuestionStatistics.builder()
                        .questionId(question.getId())
                        .questionTitle(question.getTitle())
                        .questionType(question.getQuestionType())
                        .responseCount(responseCount)
                        .responseRate(responseRate);

        if (question.getQuestionType().isChoiceType()) {
            // 선택형 질문 통계
            List<SurveyStatisticsResult.ChoiceStatistics> choiceStats = 
                    calculateChoiceStatistics(question, answersForQuestion, responseCount, totalResponseCount);
            builder.choiceStatistics(choiceStats);
        } else {
            // 텍스트형 질문 통계
            SurveyStatisticsResult.TextStatistics textStats = 
                    calculateTextStatistics(answersForQuestion);
            builder.textStatistics(textStats);
        }

        return builder.build();
    }

    /**
     * 선택형 질문 통계 계산
     */
    private List<SurveyStatisticsResult.ChoiceStatistics> calculateChoiceStatistics(
            SurveyQuestion question, List<SurveyAnswer> answers, long responseCount, long totalResponseCount) {
        
        // 선택지별 선택 횟수 계산
        Map<String, Long> choiceCountMap = new HashMap<>();
        
        // 원본 선택지들로 초기화
        for (String option : question.getOptions()) {
            choiceCountMap.put(option, 0L);
        }

        // 실제 응답 집계
        for (SurveyAnswer answer : answers) {
            for (String answerValue : answer.getAllAnswers()) {
                choiceCountMap.merge(answerValue, 1L, Long::sum);
            }
        }

        return choiceCountMap.entrySet().stream()
                .map(entry -> {
                    String choiceText = entry.getKey();
                    long count = entry.getValue();
                    double percentage = responseCount > 0 ? (double) count / responseCount * 100 : 0.0;
                    double percentageOfTotal = totalResponseCount > 0 ? (double) count / totalResponseCount * 100 : 0.0;

                    return SurveyStatisticsResult.ChoiceStatistics.builder()
                            .choiceText(choiceText)
                            .count(count)
                            .percentage(percentage)
                            .percentageOfTotal(percentageOfTotal)
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount())) // 선택 횟수 내림차순
                .collect(Collectors.toList());
    }

    /**
     * 텍스트형 질문 통계 계산
     */
    private SurveyStatisticsResult.TextStatistics calculateTextStatistics(List<SurveyAnswer> answers) {
        if (answers.isEmpty()) {
            return SurveyStatisticsResult.TextStatistics.builder()
                    .averageLength(0.0)
                    .maxLength(0)
                    .minLength(0)
                    .emptyResponseCount(0)
                    .topKeywords(Collections.emptyList())
                    .build();
        }

        List<String> allTexts = answers.stream()
                .filter(answer -> !answer.isEmpty())
                .flatMap(answer -> answer.getAllAnswers().stream())
                .filter(text -> text != null && !text.trim().isEmpty())
                .collect(Collectors.toList());

        long emptyResponseCount = answers.stream()
                .mapToLong(answer -> answer.isEmpty() ? 1 : 0)
                .sum();

        if (allTexts.isEmpty()) {
            return SurveyStatisticsResult.TextStatistics.builder()
                    .averageLength(0.0)
                    .maxLength(0)
                    .minLength(0)
                    .emptyResponseCount(emptyResponseCount)
                    .topKeywords(Collections.emptyList())
                    .build();
        }

        double averageLength = allTexts.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);

        int maxLength = allTexts.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        int minLength = allTexts.stream()
                .mapToInt(String::length)
                .min()
                .orElse(0);

        List<SurveyStatisticsResult.KeywordFrequency> topKeywords = 
                extractTopKeywords(allTexts);

        return SurveyStatisticsResult.TextStatistics.builder()
                .averageLength(averageLength)
                .maxLength(maxLength)
                .minLength(minLength)
                .emptyResponseCount(emptyResponseCount)
                .topKeywords(topKeywords)
                .build();
    }

    /**
     * 상위 키워드 추출 (간단한 구현)
     */
    private List<SurveyStatisticsResult.KeywordFrequency> extractTopKeywords(List<String> texts) {
        Map<String, Long> keywordCount = new HashMap<>();
        long totalWords = 0;

        for (String text : texts) {
            String[] words = text.toLowerCase()
                    .replaceAll("[^가-힣a-zA-Z0-9\\s]", "") // 특수문자 제거
                    .split("\\s+");
            
            for (String word : words) {
                if (word.length() >= 2) { // 2글자 이상만 키워드로 인정
                    keywordCount.merge(word, 1L, Long::sum);
                    totalWords++;
                }
            }
        }

        final long finalTotalWords = totalWords;
        
        return keywordCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2) // 2번 이상 등장한 키워드만
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> {
                    String keyword = entry.getKey();
                    long frequency = entry.getValue();
                    double percentage = finalTotalWords > 0 ? (double) frequency / finalTotalWords * 100 : 0.0;
                    
                    return new SurveyStatisticsResult.KeywordFrequency(keyword, frequency, percentage);
                })
                .collect(Collectors.toList());
    }

    /**
     * 응답 트렌드 계산
     */
    private SurveyStatisticsResult.ResponseTrend calculateResponseTrend(String surveyId) {
        List<Object[]> dailyCounts = surveyResponseRepository.getDailyResponseCounts(surveyId, 30);

        // 일별 응답 수 맵 생성
        Map<String, Long> dailyResponseCount = new LinkedHashMap<>();
        
        // 최근 30일간의 날짜를 모두 초기화 (0으로)
        LocalDate startDate = LocalDate.now().minusDays(29);
        for (int i = 0; i < 30; i++) {
            LocalDate date = startDate.plusDays(i);
            dailyResponseCount.put(date.format(DateTimeFormatter.ISO_LOCAL_DATE), 0L);
        }

        // 실제 응답 데이터로 업데이트
        for (Object[] row : dailyCounts) {
            Date date = (Date) row[0];
            Long count = (Long) row[1];
            
            String dateStr = date.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            dailyResponseCount.put(dateStr, count);
        }

        // 최근 7일 평균 계산
        double averageResponsesLast7Days = dailyResponseCount.entrySet().stream()
                .skip(Math.max(0, dailyResponseCount.size() - 7))
                .mapToLong(Map.Entry::getValue)
                .average()
                .orElse(0.0);

        // 최고 응답 날짜 찾기
        Map.Entry<String, Long> peakEntry = dailyResponseCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        String peakDate = peakEntry != null ? peakEntry.getKey() : null;
        long peakCount = peakEntry != null ? peakEntry.getValue() : 0;

        return SurveyStatisticsResult.ResponseTrend.builder()
                .dailyResponseCount(dailyResponseCount)
                .averageResponsesLast7Days(averageResponsesLast7Days)
                .peakDate(peakDate)
                .peakCount(peakCount)
                .build();
    }
}
