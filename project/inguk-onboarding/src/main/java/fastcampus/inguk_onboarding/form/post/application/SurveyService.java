package fastcampus.inguk_onboarding.form.post.application;

import fastcampus.inguk_onboarding.form.post.application.dto.*;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyItem;
import fastcampus.inguk_onboarding.form.post.domain.Survey;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyContent;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyRepository;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyVersionRepository;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SurveyService {

    private final JpaSurveyRepository surveyRepository;
    private final JpaSurveyVersionRepository surveyVersionRepository;

    public SurveyService(JpaSurveyRepository surveyRepository, JpaSurveyVersionRepository surveyVersionRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyVersionRepository = surveyVersionRepository;
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
        
        // 4. Survey 도메인을 통한 업데이트 로직 실행
        Survey survey = new Survey(surveyContent);
        SurveyEntity updatedSurvey = survey.updateSurvey(existingSurvey);
        
        // 5. 영속화 및 응답
        SurveyEntity savedSurvey = surveyRepository.save(updatedSurvey);
        
        return new SurveyResponseDto(savedSurvey);
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
