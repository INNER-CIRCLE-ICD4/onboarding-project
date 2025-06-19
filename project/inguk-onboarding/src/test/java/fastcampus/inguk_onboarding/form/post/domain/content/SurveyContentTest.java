package fastcampus.inguk_onboarding.form.post.domain.content;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class SurveyContentTest {

    @Test
    @DisplayName("정상적인 SurveyContent 생성")
    void createSurveyContent_success() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("이름", "이름을 입력해주세요", InputType.SHORT_TYPE, true, 1, null),
                new SurveyItem("만족도", "만족도를 선택해주세요", InputType.SINGLE_TYPE, true, 2,
                          List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족"))
        );

        // when
        SurveyContent surveyContent = new SurveyContent(
                "고객 만족도 조사",
                "서비스 개선을 위한 설문조사",
                items
        );

        // then
        assertThat(surveyContent.getName()).isEqualTo("고객 만족도 조사");
        assertThat(surveyContent.getDescription()).isEqualTo("서비스 개선을 위한 설문조사");
        assertThat(surveyContent.getItems()).hasSize(2);
        assertThat(surveyContent.getItemCount()).isEqualTo(2);
        assertThat(surveyContent.hasValidItemCount()).isTrue();
    }

    @Test
    @DisplayName("SurveyContent 생성 실패 - 이름이 null")
    void createSurveyContent_name_null() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("이름", "이름을 입력해주세요", InputType.SHORT_TYPE, true, 1, null)
        );

        // when & then
        assertThatThrownBy(() -> new SurveyContent(
                null,
                "설명",
                items
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("SurveyContent 생성 실패 - 이름이 빈 문자열")
    void createSurveyContent_name_empty() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("이름", "이름을 입력해주세요", InputType.SHORT_TYPE, true, 1, null)
        );

        // when & then
        assertThatThrownBy(() -> new SurveyContent(
                "   ",
                "설명",
                items
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("SurveyContent 생성 실패 - 항목이 없음")
    void createSurveyContent_content_empty() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyContent(
                "설문조사",
                "설명",
                Collections.emptyList()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("설문 받을 항목은 필수입니다.");
    }

    @Test
    @DisplayName("SurveyContent 생성 실패 - 항목이 null")
    void createSurveyContent_content_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyContent(
                "설문조사",
                "설명",
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("설문 받을 항목은 필수입니다.");
    }

    @Test
    @DisplayName("SurveyContent 생성 실패 - 항목이 10개 초과")
    void createSurveyContent_options_over() {
        // given - 11개의 항목 생성
        List<SurveyItem> items = IntStream.range(1, 12)
                .mapToObj(i -> new SurveyItem(
                        "항목" + i,
                        "설명" + i,
                        InputType.SHORT_TYPE,
                        true,
                        i,
                        null
                ))
                .toList();

        // when & then
        assertThatThrownBy(() -> new SurveyContent(
                "설문조사",
                "설명",
                items
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다.");
    }

    @Test
    @DisplayName("SurveyContent - 최대 10개 항목은 정상 생성")
    void createSurveyContent_options_correct() {
        // given - 정확히 10개의 항목 생성
        List<SurveyItem> items = IntStream.range(1, 11)
                .mapToObj(i -> new SurveyItem(
                        "항목" + i,
                        "설명" + i,
                        InputType.SHORT_TYPE,
                        true,
                        i,
                        null
                ))
                .toList();

        // when
        SurveyContent surveyContent = new SurveyContent(
                "설문조사",
                "설명",
                items
        );

        // then
        assertThat(surveyContent.getItemCount()).isEqualTo(10);
        assertThat(surveyContent.hasValidItemCount()).isTrue();
    }

    @Test
    @DisplayName("SurveyContent - 상속받은 Content의 기능 확인")
    void surveyContent_check_extends() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("이름", "이름을 입력해주세요", InputType.SHORT_TYPE, true, 1, null)
        );

        // when
        SurveyContent surveyContent = new SurveyContent(
                "고객 만족도 조사",
                "서비스 개선을 위한 설문조사",
                items
        );

        // then - Content에서 상속받은 메서드들 확인
        assertThat(surveyContent).isInstanceOf(Content.class);
        assertThat(surveyContent.getName()).isEqualTo("고객 만족도 조사");
        assertThat(surveyContent.getDescription()).isEqualTo("서비스 개선을 위한 설문조사");
    }

    @Test
    @DisplayName("SurveyContent - equals와 hashCode 정상 동작")
    void surveyContent_equals_hashCode() {
        // given
        List<SurveyItem> items = List.of(
                new SurveyItem("이름", "이름을 입력해주세요", InputType.SHORT_TYPE, true, 1, null)
        );

        SurveyContent surveyContent1 = new SurveyContent("설문조사", "설명", items);
        SurveyContent surveyContent2 = new SurveyContent("설문조사", "설명", items);
        SurveyContent surveyContent3 = new SurveyContent("다른설문조사", "설명", items);

        // when & then
        assertThat(surveyContent1).isEqualTo(surveyContent2);
        assertThat(surveyContent1).isNotEqualTo(surveyContent3);
        assertThat(surveyContent1.hashCode()).isEqualTo(surveyContent2.hashCode());
    }
} 