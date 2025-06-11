package com.fastcampus.survey.questionary.adapter.out.impl;

import com.fastcampus.survey.questionary.adapter.in.mapper.SurveyFormJpaEntityMapper;
import com.fastcampus.survey.questionary.domain.model.SurveyContent;
import com.fastcampus.survey.questionary.domain.model.SurveyContentOption;
import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import com.fastcampus.survey.questionary.domain.repository.SurveyFormRepository;
import fastcampus_inner_circle.junbeom_onboarding.JunbeomOnboardingApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JunbeomOnboardingApplication.class)
@ActiveProfiles("test") // test 프로파일 사용
class SurveyFormRepositoryImplTest {

    @Mock
    private SurveyFormRepository surveyFormRepository;

    @Mock
    private SurveyFormJpaEntityMapper surveyFormJpaEntityMapper;

    @BeforeEach
    void setUp() {
        // 각 테스트 전 H2 DB 초기화 (필요 시)
    }



    @Test
    void testSaveSurveyForm() {
        // Given
        SurveyContentOption option1 = SurveyContentOption.builder()
                .text("Option 1")
                .build();
        SurveyContent content1 = SurveyContent.builder()
                .name("Question 1")
                .describe("Description 1")
                .type(null)
                .isRequired(true)
                .options(Arrays.asList(option1))
                .build();
        SurveyForm form = SurveyForm.builder()
                .name("Test Form")
                .describe("Test Description")
                .createAt(LocalDateTime.now())
                .contents(Arrays.asList(content1))
                .build();

        // When
        SurveyForm savedForm = surveyFormRepository.save(form);

        // Then
        assertNotNull(savedForm.getId(), "Saved form should have an ID");
        assertEquals("Test Form", savedForm.getName(), "Name should match");
        assertEquals(1, savedForm.getContents().size(), "Should have one content");
        assertEquals(1, savedForm.getContents().get(0).getOptions().size(), "Should have one option");
        assertEquals("Option 1", savedForm.getContents().get(0).getOptions().get(0).getText(), "Option text should match");

        // 추가 검증: 데이터베이스에서 조회
        Optional<SurveyForm> retrievedForm = surveyFormRepository.findById(savedForm.getId());
        assertTrue(retrievedForm.isPresent(), "Form should be retrievable");
        assertEquals(savedForm.getName(), retrievedForm.get().getName(), "Retrieved name should match");
    }
}