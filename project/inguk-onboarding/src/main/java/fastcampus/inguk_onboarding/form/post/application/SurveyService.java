package fastcampus.inguk_onboarding.form.post.application;

import fastcampus.inguk_onboarding.form.post.application.dto.*;
import fastcampus.inguk_onboarding.form.post.application.interfaces.SurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyItem;
import fastcampus.inguk_onboarding.form.post.domain.Survey;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyContent;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyRepository;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyVersionRepository;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.response.application.interfaces.ResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SurveyService {

    private final JpaSurveyRepository surveyRepository;
    private final JpaSurveyVersionRepository surveyVersionRepository;
    private final ResponseRepository responseRepository;

    public SurveyService(JpaSurveyRepository surveyRepository, 
                        JpaSurveyVersionRepository surveyVersionRepository,
                        ResponseRepository responseRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyVersionRepository = surveyVersionRepository;
        this.responseRepository = responseRepository;
    }

    @Transactional
    public SurveyResponseDto createSurvey(CreateSurveyRequestDto dto) {
        // DTO → Domain 변환
        SurveyContent surveyContent = convertToSurveyContent(dto);
        
        // 도메인 객체 생성 및 비즈니스 로직 실행
        Survey survey = new Survey(surveyContent);
        SurveyEntity surveyEntity = survey.createSurvey();
        
        // 영속화 및 응답
        SurveyEntity savedSurvey = surveyRepository.save(surveyEntity);
        return new SurveyResponseDto(savedSurvey);
    }

        //update Survey
    @Transactional
    public SurveyResponseDto updateSurvey(Long surveyId, UpdateSurveyRequestDto dto) {
        // 1. URL 경로의 surveyId와 DTO의 id 일치 여부 검증 (선택적)
        if (dto.id() != null && !dto.id().equals(surveyId)) {
            throw new IllegalArgumentException("URL의 설문조사 ID와 요청 데이터의 ID가 일치하지 않습니다.");
        }
        
        // 2. 기존 설문조사 조회 (URL 경로의 ID 사용) - options까지 함께 로드
        SurveyEntity existingSurvey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다. ID: " + surveyId));
        
        // 2-1. options 컬렉션을 미리 초기화 (비교 로직에서 사용하기 전에)
        existingSurvey.getVersions().forEach(version -> 
            version.getItems().forEach(item -> {
                if (item.getOptions() != null) {
                    item.getOptions().size(); // lazy collection 초기화
                }
            })
        );
        
        // 3. DTO → Domain 변환
        SurveyContent surveyContent = convertToSurveyContent(dto);
        
        // 4. 기존 응답 영향도 확인 및 로깅
        checkExistingResponseImpact(surveyId, existingSurvey, surveyContent);
        
        // 5. Survey 도메인을 통한 업데이트 로직 실행
        Survey survey = new Survey(surveyContent);
        SurveyEntity updatedSurvey = survey.updateSurvey(existingSurvey);
        
        // 6. 영속화 및 응답
        SurveyEntity savedSurvey = surveyRepository.save(updatedSurvey);
        
        return new SurveyResponseDto(savedSurvey);
    }

    /**
     * 설문 항목 변경이 기존 응답에 미치는 영향을 확인하고 로깅
     */
    private void checkExistingResponseImpact(Long surveyId, SurveyEntity existingSurvey, SurveyContent newContent) {
        try {
            // 기존 응답 수 조회
            int existingResponseCount = responseRepository.findBySurveyId(surveyId).size();
            
            if (existingResponseCount > 0) {
                // 항목 변경 여부 확인
                boolean itemsChanged = newContent.hasItemsChangedFrom(existingSurvey);
                
                if (itemsChanged) {
                    log.warn("⚠️ 설문 항목 변경으로 인한 기존 응답 영향 알림");
                    log.warn("설문조사 ID: {}", surveyId);
                    log.warn("영향받는 기존 응답 수: {}개", existingResponseCount);
                    log.warn("기존 응답은 유지되며, 새로운 버전이 생성됩니다.");
                    
                    // 현재 최신 버전 정보
                    int currentVersionCount = existingSurvey.getVersions().size();
                    log.info("현재 버전 수: {}개, 새 버전: v{}", currentVersionCount, currentVersionCount + 1);
                } else {
                    log.info("기본 정보만 변경되어 기존 응답에는 영향이 없습니다. 응답 수: {}개", existingResponseCount);
                }
            } else {
                log.info("기존 응답이 없으므로 자유롭게 수정할 수 있습니다.");
            }
        } catch (Exception e) {
            log.error("기존 응답 영향도 확인 중 오류 발생: {}", e.getMessage());
            // 오류가 발생해도 설문 수정은 계속 진행
        }
    }

    /**
     * 공통 DTO → Domain 변환 메서드
     * CreateSurveyRequestDto와 UpdateSurveyRequestDto 모두에서 재사용 가능
     */
    private SurveyContent convertToSurveyContent(SurveyRequestDto dto) {
        List<SurveyItem> items = dto.items().stream()
                .map(this::convertToSurveyItem)
                .toList();
        
        return new SurveyContent(dto.name(), dto.description(), items);
    }
    
    private SurveyItem convertToSurveyItem(CreateSurveyItemRequestDto itemDto) {
        return new SurveyItem(
                itemDto.name(),
                itemDto.description(),
                itemDto.inputType(),
                itemDto.required(),
                itemDto.order(),
                itemDto.options()
        );
    }
}
