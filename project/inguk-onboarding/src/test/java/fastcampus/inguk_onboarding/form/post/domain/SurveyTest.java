package fastcampus.inguk_onboarding.form.post.domain;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyContent;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyItem;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SurveyTest {

    @Test
    @DisplayName("정상적인 Survey 생성 및 createSurvey 실행")
    void createSurvey_정상() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("이름", "이름을 입력해주세요", InputType.SHORT_TYPE, true, 1, null),
                new SurveyItem("만족도", "만족도를 선택해주세요", InputType.SINGLE_TYPE, true, 2,
                          List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족"))
        );

        SurveyContent surveyContent = new SurveyContent(
                "고객 만족도 조사",
                "서비스 개선을 위한 설문조사",
                items
        );

        // when
        Survey survey = new Survey(surveyContent);
        SurveyEntity surveyEntity = survey.createSurvey();

        // then
        // 설문조사 기본 정보 검증
        assertThat(surveyEntity.getTitle()).isEqualTo("고객 만족도 조사");
        assertThat(surveyEntity.getDescription()).isEqualTo("서비스 개선을 위한 설문조사");

        // 버전 정보 검증
        assertThat(surveyEntity.getVersions()).hasSize(1);
        SurveyVersionEntity version = surveyEntity.getVersions().get(0);
        assertThat(version.getVersionCode()).isEqualTo("v1");

        // 설문 항목 검증
        assertThat(version.getItems()).hasSize(2);
        
        SurveyItemEntity firstItem = version.getItems().get(0);
        assertThat(firstItem.getTitle()).isEqualTo("이름");
        assertThat(firstItem.getDescription()).isEqualTo("이름을 입력해주세요");
        assertThat(firstItem.getInputType()).isEqualTo(InputType.SHORT_TYPE);
        assertThat(firstItem.getRequired()).isTrue();
        assertThat(firstItem.getOrder()).isEqualTo(1);
        assertThat(firstItem.getOptions()).isNull();

        SurveyItemEntity secondItem = version.getItems().get(1);
        assertThat(secondItem.getTitle()).isEqualTo("만족도");
        assertThat(secondItem.getDescription()).isEqualTo("만족도를 선택해주세요");
        assertThat(secondItem.getInputType()).isEqualTo(InputType.SINGLE_TYPE);
        assertThat(secondItem.getRequired()).isTrue();
        assertThat(secondItem.getOrder()).isEqualTo(2);
        assertThat(secondItem.getOptions()).hasSize(5);
        assertThat(secondItem.getOptions()).contains("매우 만족", "만족", "보통", "불만족", "매우 불만족");
    }

    @Test
    @DisplayName("Survey 생성 시 항목 개수 검증 - 11개 항목으로 실패")
    void createSurvey_실패_항목11개() {
        // given - 11개 항목 생성
        List<SurveyItem> items = List.of(
                new SurveyItem("항목1", "설명1", InputType.SHORT_TYPE, true, 1, null),
                new SurveyItem("항목2", "설명2", InputType.SHORT_TYPE, true, 2, null),
                new SurveyItem("항목3", "설명3", InputType.SHORT_TYPE, true, 3, null),
                new SurveyItem("항목4", "설명4", InputType.SHORT_TYPE, true, 4, null),
                new SurveyItem("항목5", "설명5", InputType.SHORT_TYPE, true, 5, null),
                new SurveyItem("항목6", "설명6", InputType.SHORT_TYPE, true, 6, null),
                new SurveyItem("항목7", "설명7", InputType.SHORT_TYPE, true, 7, null),
                new SurveyItem("항목8", "설명8", InputType.SHORT_TYPE, true, 8, null),
                new SurveyItem("항목9", "설명9", InputType.SHORT_TYPE, true, 9, null),
                new SurveyItem("항목10", "설명10", InputType.SHORT_TYPE, true, 10, null),
                new SurveyItem("항목11", "설명11", InputType.SHORT_TYPE, true, 11, null)
        );

        // when & then
        assertThatThrownBy(() -> new SurveyContent(
                "설문조사",
                "설명",
                items
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다.");
    }

    @Test
    @DisplayName("Survey 생성 시 다중 선택형 항목 포함")
    void createSurvey_정상_다중선택형포함() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("취미", "관심있는 취미를 모두 선택해주세요", InputType.MULTIPLE_TYPE, false, 1,
                          List.of("영화감상", "독서", "운동", "게임", "여행"))
        );

        SurveyContent surveyContent = new SurveyContent(
                "취미 조사",
                "개인 취미에 대한 조사",
                items
        );

        // when
        Survey survey = new Survey(surveyContent);
        SurveyEntity surveyEntity = survey.createSurvey();

        // then
        SurveyVersionEntity version = surveyEntity.getVersions().get(0);
        SurveyItemEntity item = version.getItems().get(0);
        
        assertThat(item.getTitle()).isEqualTo("취미");
        assertThat(item.getInputType()).isEqualTo(InputType.MULTIPLE_TYPE);
        assertThat(item.getRequired()).isFalse();
        assertThat(item.getOptions()).hasSize(5);
        assertThat(item.getOptions()).contains("영화감상", "독서", "운동", "게임", "여행");
    }

    @Test
    @DisplayName("Survey 생성 시 장문형 항목 포함")
    void createSurvey_정상_장문형포함() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("의견", "개선사항이나 의견을 자유롭게 작성해주세요", InputType.LONG_TYPE, false, 1, null)
        );

        SurveyContent surveyContent = new SurveyContent(
                "의견 조사",
                "고객 의견 수렴을 위한 조사",
                items
        );

        // when
        Survey survey = new Survey(surveyContent);
        SurveyEntity surveyEntity = survey.createSurvey();

        // then
        SurveyVersionEntity version = surveyEntity.getVersions().get(0);
        SurveyItemEntity item = version.getItems().get(0);
        
        assertThat(item.getTitle()).isEqualTo("의견");
        assertThat(item.getInputType()).isEqualTo(InputType.LONG_TYPE);
        assertThat(item.getRequired()).isFalse();
        assertThat(item.getOptions()).isNull();
    }
} 