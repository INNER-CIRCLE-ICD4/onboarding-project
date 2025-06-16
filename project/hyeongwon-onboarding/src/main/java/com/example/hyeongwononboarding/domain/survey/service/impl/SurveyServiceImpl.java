package com.example.hyeongwononboarding.domain.survey.service.impl;

import com.example.hyeongwononboarding.common.exception.NotFoundException;
import com.example.hyeongwononboarding.common.util.UUIDGenerator;
import com.example.hyeongwononboarding.domain.survey.dto.request.CreateQuestionRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.CreateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.QuestionResponse;
import com.example.hyeongwononboarding.domain.survey.dto.response.SurveyResponse;
import com.example.hyeongwononboarding.domain.survey.entity.*;
import com.example.hyeongwononboarding.domain.survey.repository.*;
import com.example.hyeongwononboarding.domain.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 설문조사 서비스 구현체
 * 설문 생성, 조회 등 핵심 비즈니스 로직을 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyVersionRepository surveyVersionRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    /**
     * 새로운 설문조사를 생성합니다.
     *
     * @param request 설문조사 생성 요청 DTO
     * @return 생성된 설문조사 응답 DTO
     */
    @Override
    @Transactional
    public SurveyResponse createSurvey(CreateSurveyRequest request) {
        // 1. 설문 기본 정보 저장
        LocalDateTime now = LocalDateTime.now();
        Survey survey = Survey.builder()
                .id(UUIDGenerator.generate())
                .title(request.getTitle())  // 설문 제목 설정
                .description(request.getDescription())  // 설문 설명 설정
                .createdAt(now)  // 생성 시간 설정
                .build();
        survey = surveyRepository.save(survey);

        // 2. 설문 버전 정보 저장
        SurveyVersion surveyVersion = SurveyVersion.builder()
                .id(UUIDGenerator.generate())
                .surveyId(survey.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .version(1) // 첫 버전은 1로 설정
                .createdAt(now)
                .build();
        surveyVersion = surveyVersionRepository.save(surveyVersion);

        // 3. 질문 및 옵션 생성
        List<QuestionResponse> questionResponses = createQuestionsAndOptions(surveyVersion, request.getQuestions());

        // 4. 응답 DTO 생성 및 반환
        return buildSurveyResponse(survey, surveyVersion, questionResponses);
    }

    /**
     * 모든 설문조사 목록을 조회합니다.
     *
     * @return 설문조사 응답 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<SurveyResponse> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(survey -> {
                    // 1. 각 설문의 최신 버전 조회
                    SurveyVersion latestVersion = surveyVersionRepository
                            .findTopBySurveyOrderByVersionDesc(survey)
                            .orElseThrow(() -> new NotFoundException("설문 버전을 찾을 수 없습니다."));
                    
                    // 2. 해당 버전의 질문 목록 조회 (순서대로 정렬)
                    List<SurveyQuestion> questions = surveyQuestionRepository
                            .findBySurveyVersionOrderByQuestionOrderAsc(latestVersion);
                    
                    // 3. 응답 DTO 생성
                    return buildSurveyResponse(survey, latestVersion, mapToQuestionResponses(questions));
                })
                .collect(Collectors.toList());
    }

    /**
     * ID로 설문조사를 조회합니다.
     *
     * @param surveyId 조회할 설문조사 ID
     * @return 설문조사 응답 DTO
     * @throws NotFoundException 지정된 ID의 설문조사가 존재하지 않는 경우
     */
    @Override
    @Transactional(readOnly = true)
    public SurveyResponse getSurveyById(String surveyId) {
        // 1. 설문 기본 정보 조회
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException("설문을 찾을 수 없습니다: " + surveyId));
        
        // 2. 최신 버전 조회
        SurveyVersion latestVersion = surveyVersionRepository
                .findTopBySurveyOrderByVersionDesc(survey)
                .orElseThrow(() -> new NotFoundException("설문 버전을 찾을 수 없습니다."));
        
        // 3. 질문 목록 조회 (순서대로 정렬)
        List<SurveyQuestion> questions = surveyQuestionRepository
                .findBySurveyVersionOrderByQuestionOrderAsc(latestVersion);
        
        // 4. 응답 DTO 생성 및 반환
        return buildSurveyResponse(survey, latestVersion, mapToQuestionResponses(questions));
    }

    /**
     * 설문 버전에 대한 질문과 옵션을 생성합니다.
     *
     * @param surveyVersion 설문 버전 엔티티
     * @param questionRequests 질문 생성 요청 목록
     * @return 생성된 질문 응답 DTO 목록
     */
    private List<QuestionResponse> createQuestionsAndOptions(
            SurveyVersion surveyVersion, 
            List<CreateQuestionRequest> questionRequests) {
        
        if (questionRequests == null || questionRequests.isEmpty()) {
            return Collections.emptyList();
        }

        List<QuestionResponse> questionResponses = new ArrayList<>();
        
        for (CreateQuestionRequest questionRequest : questionRequests) {
            // 1. 질문 엔티티 생성 및 저장
            SurveyQuestion question = SurveyQuestion.builder()
                    .id(UUIDGenerator.generate())
                    .surveyVersionId(surveyVersion.getId())
                    .name(questionRequest.getName())
                    .description(questionRequest.getDescription())
                    .inputType(questionRequest.getInputType())
                    .isRequired(questionRequest.getIsRequired())
                    .questionOrder(questionRequest.getOrder())
                    .build();
            
            question = surveyQuestionRepository.save(question);
            
            // 2. 옵션 생성 (필요한 경우)
            List<QuestionResponse.OptionResponse> optionResponses = 
                    createOptionsForQuestion(question, questionRequest.getOptions());
            
            // 3. 질문 응답 DTO 생성
            questionResponses.add(buildQuestionResponse(question, optionResponses));
        }
        
        return questionResponses;
    }

    /**
     * 질문에 대한 옵션을 생성합니다.
     *
     * @param question 질문 엔티티
     * @param optionRequests 옵션 생성 요청 목록
     * @return 생성된 옵션 응답 DTO 목록
     */
    private List<QuestionResponse.OptionResponse> createOptionsForQuestion(
            SurveyQuestion question,
            List<String> optionTexts) {
        
        if (optionTexts == null || optionTexts.isEmpty()) {
            return Collections.emptyList();
        }

        List<QuestionResponse.OptionResponse> optionResponses = new ArrayList<>();
        
        if (optionTexts != null) {
            int order = 1;
            for (String text : optionTexts) {
                // 1. 옵션 엔티티 생성 및 저장
                QuestionOption option = QuestionOption.builder()
                        .id(UUIDGenerator.generate())
                        .questionId(question.getId())
                        .optionText(text)
                        .optionOrder(order++)
                        .build();
                
                option = questionOptionRepository.save(option);
                
                // 2. 옵션 응답 DTO 생성
                optionResponses.add(QuestionResponse.OptionResponse.builder()
                        .id(option.getId())
                        .text(option.getOptionText())
                        .order(option.getOptionOrder())
                        .build());
            }
        }
        
        return optionResponses;
    }

    /**
     * SurveyQuestion 엔티티 리스트를 QuestionResponse DTO 리스트로 변환합니다.
     *
     * @param questions 설문 질문 엔티티 리스트
     * @return QuestionResponse DTO 리스트
     */
    private List<QuestionResponse> mapToQuestionResponses(List<SurveyQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            return Collections.emptyList();
        }

        return questions.stream()
                .map(question -> {
                    // 1. 옵션 응답 DTO 리스트 생성
                    List<QuestionResponse.OptionResponse> optionResponses = 
                            question.getOptions() != null ? 
                            question.getOptions().stream()
                                    .map(option -> QuestionResponse.OptionResponse.builder()
                                            .id(option.getId())
                                            .text(option.getOptionText())
                                            .order(option.getOptionOrder())
                                            .build())
                                    .collect(Collectors.toList()) :
                            Collections.emptyList();

                    // 2. 질문 응답 DTO 생성
                    return buildQuestionResponse(question, optionResponses);
                })
                .collect(Collectors.toList());
    }

    /**
     * 설문 응답 DTO를 생성합니다.
     *
     * @param survey 설문 엔티티
     * @param version 설문 버전 엔티티
     * @param questions 질문 응답 DTO 목록
     * @return 설문 응답 DTO
     */
    private SurveyResponse buildSurveyResponse(
            Survey survey,
            SurveyVersion version,
            List<QuestionResponse> questions) {
        
        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .title(version.getTitle())
                .description(version.getDescription())
                .version(version.getVersion())
                .createdAt(survey.getCreatedAt())  // Survey 엔티티의 createdAt 사용
                .questions(questions)
                .build();
    }

    /**
     * 질문 응답 DTO를 생성합니다.
     *
     * @param question 질문 엔티티
     * @param options 옵션 응답 DTO 목록
     * @return 질문 응답 DTO
     */
    private QuestionResponse buildQuestionResponse(
            SurveyQuestion question,
            List<QuestionResponse.OptionResponse> options) {
        
        return QuestionResponse.builder()
                .id(question.getId())
                .name(question.getName())
                .description(question.getDescription())
                .inputType(question.getInputType())
                .isRequired(question.getIsRequired())
                .order(question.getQuestionOrder())
                .options(options)
                .build();
    }
}
