package com.example.hyeongwononboarding.domain.survey.service.impl;

import com.example.hyeongwononboarding.common.exception.NotFoundException;
import com.example.hyeongwononboarding.common.util.UUIDGenerator;
import com.example.hyeongwononboarding.domain.survey.dto.request.CreateQuestionRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.QuestionOptionRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.QuestionRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.UpdateQuestionRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.CreateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.UpdateSurveyRequest;
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
                .versionNumber(1) // 첫 버전은 1로 설정
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
                            .findTopBySurveyIdOrderByVersionNumberDesc(survey.getId())
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
        Survey survey = findSurveyById(surveyId);
        
        // 2. 최신 버전 조회
        SurveyVersion latestVersion = getLatestSurveyVersion(survey);
        
        // 3. 질문 목록 조회 (순서대로 정렬)
        List<SurveyQuestion> questions = surveyQuestionRepository
                .findBySurveyVersionOrderByQuestionOrderAsc(latestVersion);
        
        // 4. 응답 DTO 생성 및 반환
        return buildSurveyResponse(survey, latestVersion, mapToQuestionResponses(questions));
    }

    /**
     * 설문조사를 수정합니다.
     *
     * @param surveyId 수정할 설문조사 ID
     * @param request 설문조사 수정 요청 DTO
     * @return 수정된 설문조사 응답 DTO
     * @throws NotFoundException 지정된 ID의 설문조사가 존재하지 않는 경우
     */
    @Override
    @Transactional
    public SurveyResponse updateSurvey(String surveyId, UpdateSurveyRequest request) {
        // 1. 기존 설문 조회
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 설문조사 ID: " + surveyId));
        
        // 2. 기존 최신 버전 조회
        SurveyVersion currentVersion = surveyVersionRepository
                .findTopBySurveyIdOrderByVersionNumberDesc(surveyId)
                .orElseThrow(() -> new IllegalStateException("설문에 버전이 존재하지 않습니다."));
        
        // 3. 새 버전 번호 계산 (현재 버전 + 1)
        int newVersionNumber = currentVersion.getVersionNumber() + 1;
        
        // 4. 설문 기본 정보 업데이트
        survey.update(request.getTitle(), request.getDescription());
        
        // 5. 새 버전 생성 (새로운 ID 부여)
        SurveyVersion newVersion = SurveyVersion.builder()
                .id(UUIDGenerator.generate())
                .surveyId(surveyId)
                .title(request.getTitle())
                .description(request.getDescription())
                .versionNumber(newVersionNumber)
                .build();
        
        // 6. 새 버전 저장
        newVersion = surveyVersionRepository.save(newVersion);
        
        // 7. 기존 질문과 옵션을 새로운 버전으로 복사
        List<QuestionResponse> questionResponses = new ArrayList<>();
        
        // 8. 요청된 질문들을 순회하면서 처리
        for (UpdateQuestionRequest questionRequest : request.getQuestions()) {
            // 8-1. 새 질문 생성 (항상 새로운 ID 생성)
            String newQuestionId = UUIDGenerator.generate();
                    
            SurveyQuestion newQuestion = SurveyQuestion.builder()
                    .id(newQuestionId)
                    .surveyVersion(newVersion)
                    .name(questionRequest.getName())
                    .description(questionRequest.getDescription())
                    .inputType(questionRequest.getInputType())
                    .isRequired(questionRequest.getIsRequired())
                    .questionOrder(questionRequest.getOrder())
                    .build();
            
            // 8-2. 질문 저장
            newQuestion = surveyQuestionRepository.save(newQuestion);
            
            // 8-3. 이전 버전의 질문 ID와 새 질문 ID 매핑 정보 저장
            if (questionRequest.getId() != null) {
                // 설문 응답과의 매핑을 위해 이전 질문 ID와 새 질문 ID 매핑 정보 저장
                // (이 부분은 설문 응답을 처리하는 로직에서 활용)
                // 예: questionMappingService.saveQuestionMapping(previousVersionId, questionRequest.getId(), newQuestionId);
            }
            
            // 8-4. 옵션 처리
            List<QuestionResponse.OptionResponse> optionResponses = new ArrayList<>();
            if (questionRequest.getOptions() != null && !questionRequest.getOptions().isEmpty()) {
                for (QuestionOptionRequest optionRequest : questionRequest.getOptions()) {
                    // 새 옵션 생성 (항상 새로운 ID 생성)
                    QuestionOption newOption = QuestionOption.builder()
                            .id(UUIDGenerator.generate())
                            .question(newQuestion)
                            .optionText(optionRequest.getText())
                            .optionOrder(optionRequest.getOrder() != null ? optionRequest.getOrder() : 0)
                            .build();
                    
                    // 옵션 저장 (새로운 행으로 추가)
                    newOption = questionOptionRepository.save(newOption);
                    newQuestion.addOption(newOption);
                    
                    // 옵션 응답 DTO 생성
                    optionResponses.add(QuestionResponse.OptionResponse.builder()
                            .id(newOption.getId())
                            .text(newOption.getOptionText())
                            .order(newOption.getOptionOrder())
                            .build());
                    
                    // 이전 버전의 옵션 ID와 새 옵션 ID 매핑 정보 저장 (필요한 경우)
                    if (optionRequest.getId() != null) {
                        // optionMappingService.saveOptionMapping(questionRequest.getId(), optionRequest.getId(), newOption.getId());
                    }
                }
            }
            
            // 8-5. 질문 응답 DTO 생성
            questionResponses.add(QuestionResponse.builder()
                    .id(newQuestion.getId())
                    .name(newQuestion.getName())
                    .description(newQuestion.getDescription())
                    .inputType(newQuestion.getInputType())
                    .isRequired(newQuestion.getIsRequired())
                    .order(newQuestion.getQuestionOrder())
                    .options(optionResponses)
                    .build());
        }
        
        // 9. 설문의 현재 버전 번호 업데이트
        survey.updateCurrentVersion(newVersionNumber);
        
        // 10. 업데이트된 설문 저장
        survey = surveyRepository.save(survey);
        
        // 11. 응답 DTO 생성 및 반환
        return buildSurveyResponse(survey, newVersion, questionResponses);
    }

    /**
     * ID로 설문을 조회합니다.
     *
     * @param surveyId 조회할 설문 ID
     * @return 조회된 설문 엔티티
     * @throws NotFoundException 설문이 존재하지 않는 경우
     */
    private Survey findSurveyById(String surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException("요청하신 설문조사를 찾을 수 없습니다."));
    }
    
    /**
     * 설문의 최신 버전을 조회합니다.
     *
     * @param survey 설문 엔티티
     * @return 최신 버전 엔티티
     * @throws NotFoundException 버전이 존재하지 않는 경우
     */
    private SurveyVersion getLatestSurveyVersion(Survey survey) {
        return surveyVersionRepository
                .findTopBySurveyIdOrderByVersionNumberDesc(survey.getId())
                .orElseThrow(() -> new NotFoundException("설문 버전을 찾을 수 없습니다."));
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
            List<? extends QuestionRequest> questionRequests) {
        
        if (questionRequests == null || questionRequests.isEmpty()) {
            return Collections.emptyList();
        }

        return questionRequests.stream()
                .map(questionRequest -> {
                    // Always generate new ID for new version
                    String questionId = UUIDGenerator.generate();
                    
                    // Create new question with new ID
                    SurveyQuestion question = SurveyQuestion.builder()
                            .id(questionId)
                            .surveyVersion(surveyVersion)
                            .name(questionRequest.getName())
                            .description(questionRequest.getDescription())
                            .inputType(questionRequest.getInputType())
                            .isRequired(questionRequest.getIsRequired())
                            .questionOrder(questionRequest.getOrder())
                            .build();
                    
                    // Save the new question
                    question = surveyQuestionRepository.save(question);
                    
                    // Create options if any
                    List<QuestionResponse.OptionResponse> optionResponses = List.of();
                    if (questionRequest.getOptions() != null && !questionRequest.getOptions().isEmpty()) {
                        if (questionRequest instanceof CreateQuestionRequest) {
                            // Handle CreateQuestionRequest with List<String> options
                            CreateQuestionRequest createRequest = (CreateQuestionRequest) questionRequest;
                            List<QuestionOptionRequest> optionRequests = createRequest.getOptions().stream()
                                    .map(text -> new QuestionOptionRequest() {
                                        @Override
                                        public String getId() {
                                            return UUIDGenerator.generate();
                                        }
                                        @Override
                                        public String getText() {
                                            return text;
                                        }
                                        @Override
                                        public Integer getOrder() {
                                            return 0; // Default order
                                        }
                                    })
                                    .collect(Collectors.toList());
                            optionResponses = createOptionsForQuestion(question, optionRequests);
                        } else {
                            // Handle UpdateQuestionRequest with List<QuestionOptionRequest> options
                            optionResponses = createOptionsForQuestion(question, 
                                questionRequest.getOptions().stream()
                                    .map(opt -> (QuestionOptionRequest) opt)
                                    .collect(Collectors.toList()));
                        }
                    }
                    
                    // Build and return question response
                    return buildQuestionResponse(question, optionResponses);
                })
                .collect(Collectors.toList());
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
            List<QuestionOptionRequest> optionRequests) {
        
        if (optionRequests == null || optionRequests.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<QuestionResponse.OptionResponse> optionResponses = new ArrayList<>();
        for (int i = 0; i < optionRequests.size(); i++) {
            QuestionOptionRequest optionRequest = optionRequests.get(i);
            String optionId = optionRequest.getId() != null ? optionRequest.getId() : UUIDGenerator.generate();
            int optionOrder = (optionRequest.getOrder() != null && optionRequest.getOrder() > 0) ? optionRequest.getOrder() : i + 1;

            QuestionOption option = QuestionOption.builder()
                    .id(optionId)
                    .question(question)
                    .optionOrder(optionOrder)
                    .optionText(optionRequest.getText())
                    .build();
            option = questionOptionRepository.save(option);
            question.addOption(option);
            optionResponses.add(QuestionResponse.OptionResponse.builder()
                    .id(option.getId())
                    .text(option.getOptionText())
                    .order(option.getOptionOrder())
                    .build());
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
    private SurveyResponse buildSurveyResponse(Survey survey, SurveyVersion version, List<QuestionResponse> questions) {
        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .title(version.getTitle())
                .description(version.getDescription())
                .version(version.getVersionNumber())
                .createdAt(survey.getCreatedAt())
                .updatedAt(version.getCreatedAt())
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
