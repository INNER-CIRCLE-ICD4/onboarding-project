package fastcampus.inguk_onboarding.form.response.application;

import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyVersionRepository;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import fastcampus.inguk_onboarding.form.response.Response;
import fastcampus.inguk_onboarding.form.response.application.dto.ResponseSurveyRequestDto;
import fastcampus.inguk_onboarding.form.response.application.interfaces.ResponseRepository;
import fastcampus.inguk_onboarding.form.response.domain.ResponseContent;
import fastcampus.inguk_onboarding.form.response.domain.ResponseItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyResponseService {

    private final ResponseRepository responseRepository;
    private final JpaSurveyVersionRepository surveyVersionRepository;

    @Transactional
    public Response answerSurvey(Long surveyVersionId, ResponseSurveyRequestDto dto) {
        
        // 설문 버전 조회 (Survey와 Items를 함께 fetch)
        SurveyVersionEntity surveyVersion = surveyVersionRepository.findByIdWithSurveyAndItems(surveyVersionId)
                .orElseThrow(() -> new RuntimeException("설문 버전을 찾을 수 없습니다. ID: " + surveyVersionId));
        
        Long surveyId = surveyVersion.getSurvey().getId();
        List<SurveyItemEntity> surveyItems = surveyVersion.getItems();
        
        // 설문 항목이 없는 경우 확인
        if (surveyItems.isEmpty()) {
            throw new RuntimeException("설문 항목이 없습니다. 설문 버전 ID: " + surveyVersionId);
        }
        
        // 응답 값 검증
        validateSurveyResponse(surveyId, surveyVersionId, dto, surveyItems);
        
        // DTO를 도메인 객체로 변환
        Response response = Response.from(surveyId, surveyVersionId, dto);
        
        // 응답 저장
        return responseRepository.save(response);
    }

    @Transactional
    public Response getResponse(Long responseId) {
        try {
            log.info("응답 조회 시작 - responseId: {}", responseId);
            Response response = responseRepository.findById(responseId);
            log.info("응답 조회 성공 - responseId: {}, response: {}", responseId, response);
            return response;
        } catch (Exception e) {
            log.error("응답 조회 실패 - responseId: {}, 오류: {}", responseId, e.getMessage(), e);
            throw new RuntimeException("응답 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 설문 응답 검증
     */
    private void validateSurveyResponse(Long surveyId, Long surveyVersionId, ResponseSurveyRequestDto dto, List<SurveyItemEntity> surveyItems) {
        // ResponseItem 리스트 생성
        List<ResponseItem> responseItems = dto.answers().stream()
                .map(ResponseItem::from)
                .toList();
        
        // ResponseContent로 통합 검증
        ResponseContent responseContent = ResponseContent.create(
                surveyId, 
                surveyVersionId, 
                responseItems,
                surveyItems
        );
        
        // 전체 검증 수행 (validate 메서드에서 모든 검증을 수행)
        if (!responseContent.validate()) {
            throw new RuntimeException("설문 응답이 유효하지 않습니다.");
        }
        
        // 추가 비즈니스 규칙 검증
        if (responseContent.hasDuplicateOrder()) {
            throw new RuntimeException("중복된 응답 순서가 있습니다.");
        }
        
        if (!responseContent.hasValidOrder()) {
            throw new RuntimeException("응답 순서가 설문 항목 순서와 일치하지 않습니다.");
        }
        
        if (!responseContent.hasAllRequiredAnswers()) {
            throw new RuntimeException("필수 응답이 누락되었습니다.");
        }
    }
}

