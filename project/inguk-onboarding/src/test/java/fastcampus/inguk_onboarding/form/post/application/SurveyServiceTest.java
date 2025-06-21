package fastcampus.inguk_onboarding.form.post.application;

import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyItemRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyRepository;
import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyVersionRepository;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @Mock
    private JpaSurveyRepository surveyRepository;

    @Mock
    private JpaSurveyVersionRepository surveyVersionRepository;

    @InjectMocks
    private SurveyService surveyService;

    @Test
    @DisplayName("설문조사 생성 성공 - 단답형과 선택형 혼합")
    void createSurvey_successMutiSurvey() {
        // given
        CreateSurveyItemRequestDto item1 = new CreateSurveyItemRequestDto(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        );

        CreateSurveyItemRequestDto item2 = new CreateSurveyItemRequestDto(
                "만족도",
                "만족도를 선택해주세요",
                InputType.SINGLE_TYPE,
                true,
                2,
                List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족")
        );

        CreateSurveyRequestDto dto = new CreateSurveyRequestDto(
                "고객 만족도 조사",
                "서비스 개선을 위한 설문조사",
                "ACTIVE",
                List.of(item1, item2)
        );

        SurveyEntity mockSavedEntity = new SurveyEntity("고객 만족도 조사", "서비스 개선을 위한 설문조사");

        when(surveyRepository.save(any(SurveyEntity.class))).thenReturn(mockSavedEntity);

        // when
        SurveyResponseDto result = surveyService.createSurvey(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("고객 만족도 조사");
        assertThat(result.getDescription()).isEqualTo("서비스 개선을 위한 설문조사");

        verify(surveyRepository, times(1)).save(any(SurveyEntity.class));
    }

    @Test
    @DisplayName("설문조사 생성 실패 - 설문조사 이름이 없음")
    void createSurvey_Name_null() {
        // given
        CreateSurveyItemRequestDto item = new CreateSurveyItemRequestDto(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        );

        CreateSurveyRequestDto dto = new CreateSurveyRequestDto(
                null,
                "설명",
                "ACTIVE",
                List.of(item)
        );

        // when & then
        assertThatThrownBy(() -> surveyService.createSurvey(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");

        verify(surveyRepository, never()).save(any(SurveyEntity.class));
    }

    @Test
    @DisplayName("설문조사 생성 실패 - 항목이 없음")
    void createSurvey_Content_null() {
        // given
        CreateSurveyRequestDto dto = new CreateSurveyRequestDto(
                "설문조사",
                "설명",
                "ACTIVE",
                List.of()
        );

        // when & then
        assertThatThrownBy(() -> surveyService.createSurvey(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("설문 받을 항목은 필수입니다.");

        verify(surveyRepository, never()).save(any(SurveyEntity.class));
    }

    @Test
    @DisplayName("설문조사 생성 실패 - 선택형 항목에 옵션이 없음")
    void createSurvey_Option_null() {
        // given
        CreateSurveyItemRequestDto item = new CreateSurveyItemRequestDto(
                "만족도",
                "만족도를 선택해주세요",
                InputType.SINGLE_TYPE,
                true,
                1,
                null // 선택형인데 옵션이 없음
        );

        CreateSurveyRequestDto dto = new CreateSurveyRequestDto(
                "설문조사",
                "설명",
                "ACTIVE",
                List.of(item)
        );

        // when & then
        assertThatThrownBy(() -> surveyService.createSurvey(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택형 항목은 선택 옵션이 필요합니다.");

        verify(surveyRepository, never()).save(any(SurveyEntity.class));
    }

    @Test
    @DisplayName("설문조사 생성 성공 - 다중 선택형")
    void createSurvey_success_MutiSurvey() {
        // given
        CreateSurveyItemRequestDto item = new CreateSurveyItemRequestDto(
                "취미",
                "관심있는 취미를 모두 선택해주세요",
                InputType.MULTIPLE_TYPE,
                false,
                1,
                List.of("영화감상", "독서", "운동", "게임", "여행")
        );

        CreateSurveyRequestDto dto = new CreateSurveyRequestDto(
                "취미 조사",
                "개인 취미에 대한 조사",
                "ACTIVE",
                List.of(item)
        );

                 SurveyEntity mockSavedEntity = new SurveyEntity("취미 조사", "개인 취미에 대한 조사");

         when(surveyRepository.save(any(SurveyEntity.class))).thenReturn(mockSavedEntity);

        // when
        SurveyResponseDto result = surveyService.createSurvey(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("취미 조사");
        assertThat(result.getDescription()).isEqualTo("개인 취미에 대한 조사");

        verify(surveyRepository, times(1)).save(any(SurveyEntity.class));
    }
} 