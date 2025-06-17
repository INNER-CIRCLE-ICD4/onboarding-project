package fastcampus.inguk_onboarding.form.post.application;

import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyItemRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import fastcampus.inguk_onboarding.form.post.application.dto.UpdateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.domain.content.Content;
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
    public SurveyResponseDto updateSurvey(UpdateSurveyRequestDto dto) {

        return null;
    }
    private SurveyContent convertToSurveyContent(UpdateSurveyRequestDto dto) {
        List<Content> contents = dto.items().stream()
                .map(this::convertToContent)
                .toList();
        return new SurveyContent(dto.name(), dto.description(), contents);
    }

    private SurveyContent convertToSurveyContent(CreateSurveyRequestDto dto) {
        List<Content> contents = dto.items().stream()
                .map(this::convertToContent)
                .toList();
        
        return new SurveyContent(dto.name(), dto.description(), contents);
    }
    
    private Content convertToContent(CreateSurveyItemRequestDto itemDto) {
        return new Content(
                itemDto.name(),
                itemDto.description(),
                itemDto.inputType(),
                itemDto.required(),
                itemDto.order(),
                itemDto.options()
        );
    }







}
