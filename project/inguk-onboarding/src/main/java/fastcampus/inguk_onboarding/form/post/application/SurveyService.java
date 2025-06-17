package fastcampus.inguk_onboarding.form.post.application;

import fastcampus.inguk_onboarding.common.exception.InvalidSurveyItemCountException;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyItemRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyRepository;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SurveyService {

    private final JpaSurveyRepository surveyRepository;

    public SurveyService(JpaSurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Transactional
    public SurveyResponseDto createSurvey(CreateSurveyRequestDto dto) {
        validateSurveyItemCount(dto.items().size());
        
        // 설문조사 엔티티 생성
        SurveyEntity survey = new SurveyEntity(dto.name(), dto.description());
        
        // 각 설문 항목 처리
        for(CreateSurveyItemRequestDto itemRequest : dto.items()) {
            // 선택형 항목 옵션 검증
            validateChoiceOptions(itemRequest);
            
            SurveyItemEntity item = new SurveyItemEntity(
                    itemRequest.name(),
                    itemRequest.description(),
                    itemRequest.inputType(),
                    itemRequest.required(),
                    itemRequest.order()
            );
            
            if (isChoiceType(itemRequest.inputType())) {
                item.setOptions(itemRequest.options());
            }
            
            // 설문조사에 항목 추가
            survey.addItem(item);
        }
        
        SurveyEntity savedSurvey = surveyRepository.save(survey);
        return new SurveyResponseDto(savedSurvey);
    }
    
    private void validateChoiceOptions(CreateSurveyItemRequestDto dto) {
        if(isChoiceType(dto.inputType())){
            if(dto.options() == null || dto.options().isEmpty()){
                throw new IllegalArgumentException("선택형 항목은 선택 옵션이 필요합니다.");
            }
        }
    }
    
    private void validateSurveyItemCount(int count) {
        if (count < 1 || count > 10) {
            throw new InvalidSurveyItemCountException(count);
        }
    }
    
    private boolean isChoiceType(InputType inputType) {
        return inputType == InputType.SINGLE_TYPE || inputType == InputType.MULTIPLE_TYPE;
    }

}
