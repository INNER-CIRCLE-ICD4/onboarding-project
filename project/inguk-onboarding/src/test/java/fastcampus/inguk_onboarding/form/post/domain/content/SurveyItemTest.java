package fastcampus.inguk_onboarding.form.post.domain.content;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SurveyItemTest {

    @Test
    @DisplayName("정상적인 SurveyItem 생성 - 단답형")
    void createSurveyItem_Short_type() {
        // given & when
        SurveyItem surveyItem = new SurveyItem(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        );

        // then
        assertThat(surveyItem.getName()).isEqualTo("이름");
        assertThat(surveyItem.getDescription()).isEqualTo("이름을 입력해주세요");
        assertThat(surveyItem.getInputType()).isEqualTo(InputType.SHORT_TYPE);
        assertThat(surveyItem.getRequired()).isTrue();
        assertThat(surveyItem.getOrder()).isEqualTo(1);
        assertThat(surveyItem.hasOptions()).isFalse();
    }

    @Test
    @DisplayName("정상적인 SurveyItem 생성 - 단일 선택형")
    void createSurveyItem_SingleType() {
        // given & when
        SurveyItem surveyItem = new SurveyItem(
                "만족도",
                "만족도를 선택해주세요",
                InputType.SINGLE_TYPE,
                true,
                1,
                List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족")
        );

        // then
        assertThat(surveyItem.getName()).isEqualTo("만족도");
        assertThat(surveyItem.getDescription()).isEqualTo("만족도를 선택해주세요");
        assertThat(surveyItem.getInputType()).isEqualTo(InputType.SINGLE_TYPE);
        assertThat(surveyItem.hasOptions()).isTrue();
        assertThat(surveyItem.getOptions()).hasSize(5);
        assertThat(surveyItem.getOptions()).contains("매우 만족", "만족", "보통");
    }

    @Test
    @DisplayName("정상적인 SurveyItem 생성 - 다중 선택형")
    void createSurveyItem_MultipleType() {
        // given & when
        SurveyItem surveyItem = new SurveyItem(
                "취미",
                "관심있는 취미를 모두 선택해주세요",
                InputType.MULTIPLE_TYPE,
                false,
                2,
                List.of("영화감상", "독서", "운동", "게임", "여행")
        );

        // then
        assertThat(surveyItem.getName()).isEqualTo("취미");
        assertThat(surveyItem.getInputType()).isEqualTo(InputType.MULTIPLE_TYPE);
        assertThat(surveyItem.getRequired()).isFalse();
        assertThat(surveyItem.getOrder()).isEqualTo(2);
        assertThat(surveyItem.hasOptions()).isTrue();
        assertThat(surveyItem.getOptions()).hasSize(5);
    }

    @Test
    @DisplayName("정상적인 SurveyItem 생성 - 장문형")
    void createSurveyItem_LongType() {
        // given & when
        SurveyItem surveyItem = new SurveyItem(
                "의견",
                "개선사항이나 의견을 자유롭게 작성해주세요",
                InputType.LONG_TYPE,
                false,
                3,
                null
        );

        // then
        assertThat(surveyItem.getName()).isEqualTo("의견");
        assertThat(surveyItem.getInputType()).isEqualTo(InputType.LONG_TYPE);
        assertThat(surveyItem.getRequired()).isFalse();
        assertThat(surveyItem.hasOptions()).isFalse();
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 이름이 null")
    void createSurveyItem_Name_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                null,
                "설명",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 이름이 빈 문자열")
    void createSurveyItem_name_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                "   ",
                "설명",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 입력 타입이 null")
    void createSurveyItem_InputType_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                "이름",
                "설명",
                null,
                true,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("항목 입력 형태는 필수입니다.");
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 필수여부가 null")
    void createSurveyItem_required_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                "이름",
                "설명",
                InputType.SHORT_TYPE,
                null,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("항목 필수 여부는 필수입니다.");
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 순서가 null")
    void createSurveyItem_order_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                "이름",
                "설명",
                InputType.SHORT_TYPE,
                true,
                null,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("항목 순서는 필수입니다.");
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 단일선택형인데 옵션이 null")
    void createSurveyItem_singleType_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                "만족도",
                "만족도를 선택해주세요",
                InputType.SINGLE_TYPE,
                true,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("선택형 항목은 선택 옵션이 필요합니다.");
    }

    @Test
    @DisplayName("SurveyItem 생성 실패 - 다중선택형인데 옵션이 빈 리스트")
    void createSurveyItem_options_null() {
        // given & when & then
        assertThatThrownBy(() -> new SurveyItem(
                "취미",
                "취미를 선택해주세요",
                InputType.MULTIPLE_TYPE,
                false,
                2,
                List.of()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("선택형 항목은 선택 옵션이 필요합니다.");
    }

    @Test
    @DisplayName("SurveyItem 비교 - 다른 항목과 다름을 확인")
    void isDifferentFrom_differentType() {
        // given
        SurveyItem surveyItem = new SurveyItem(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        );

        // when & then
        assertThat(surveyItem.isDifferentFrom(
                "다른이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        )).isTrue();
    }

    @Test
    @DisplayName("SurveyItem 비교 - 같은 항목임을 확인")
    void isDifferentFrom_sameType() {
        // given
        SurveyItem surveyItem = new SurveyItem(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        );

        // when & then
        assertThat(surveyItem.isDifferentFrom(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        )).isFalse();
    }
} 