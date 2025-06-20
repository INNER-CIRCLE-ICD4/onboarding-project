package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.impl;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.mapper.SurveyFormJpaEntityMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.repository.SurveyFormJpaRepository;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SurveyFormRepositoryImplTest {

    @Mock
    private SurveyFormJpaRepository jpaRepository;

    @Mock
    private SurveyFormJpaEntityMapper surveyFormJpaEntityMapper;

    @InjectMocks
    private SurveyFormRepositoryImpl surveyFormRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("SurveyForm 저장 테스트 - 성공")
    void save_SurveyForm_ReturnsSavedSurveyForm() {
        // Given
        SurveyForm surveyForm = new SurveyForm();



        SurveyFormJpaEntity entity = SurveyFormJpaEntity.builder()
                .name("테스트 설문")
                .describe("테스트 설명")
                .build();

        SurveyFormJpaEntity savedEntity = SurveyFormJpaEntity.builder()
                .name("테스트 설문")
                .describe("테스트 설명")
                .build();

        SurveyForm savedSurveyForm = new SurveyForm();

        when(surveyFormJpaEntityMapper.toJpaEntity(any(SurveyForm.class))).thenReturn(entity);
        when(jpaRepository.save(any(SurveyFormJpaEntity.class))).thenReturn(savedEntity);
        when(surveyFormJpaEntityMapper.toDomain(any(SurveyFormJpaEntity.class))).thenReturn(savedSurveyForm);

        // When
        SurveyForm result = surveyFormRepository.save(surveyForm);

        // Then
        assertNotNull(result);
        assertEquals(savedSurveyForm, result);
        verify(surveyFormJpaEntityMapper, times(1)).toJpaEntity(surveyForm);
        verify(jpaRepository, times(1)).save(entity);
        verify(surveyFormJpaEntityMapper, times(1)).toDomain(savedEntity);
    }

    @Test
    void findById() {
    }
}