package fastcampus.inguk_onboarding.form.post.domain;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.post.domain.content.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ContentTest {

    @Test
    @DisplayName("정상적인 Content 생성 - 단답형")
    void createContent_Short_type() {
        // given & when
        Content content = new Content(
                "이름",
                "이름을 입력해주세요",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        );

        // then
        assertThat(content.name()).isEqualTo("이름");
        assertThat(content.inputType()).isEqualTo(InputType.SHORT_TYPE);
        assertThat(content.required()).isTrue();
        assertThat(content.hasOptions()).isFalse();
    }

    @Test
    @DisplayName("정상적인 Content 생성 - 단일 선택형")
    void createContentSingleType() {
        // given & when
        Content content = new Content(
                "만족도",
                "만족도를 선택해주세요",
                InputType.SINGLE_TYPE,
                true,
                1,
                List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족")
        );

        // then
        assertThat(content.name()).isEqualTo("만족도");
        assertThat(content.inputType()).isEqualTo(InputType.SINGLE_TYPE);
        assertThat(content.hasOptions()).isTrue();
        assertThat(content.options()).hasSize(5);
    }

    @Test
    @DisplayName("Content 생성 실패 - 이름이 null")
    void createContentShortNameIsNull() {
        // given & when & then
        assertThatThrownBy(() -> new Content(
                null,
                "설명",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("항목 이름은 필수입니다.");
    }

    @Test
    @DisplayName("Content 생성 실패 - 이름이 빈 문자열")
    void createContentNameIsNull() {
        // given & when & then
        assertThatThrownBy(() -> new Content(
                "   ",
                "설명",
                InputType.SHORT_TYPE,
                true,
                1,
                null
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("항목 이름은 필수입니다.");
    }

    @Test
    @DisplayName("Content 생성 실패 - 입력 타입이 null")
    void createContentInputTypeIsNull() {
        // given & when & then
        assertThatThrownBy(() -> new Content(
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
    @DisplayName("Content 생성 실패 - 선택형인데 옵션이 없음")
    void createContentOptionIsNull() {
        // given & when & then
        assertThatThrownBy(() -> new Content(
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
    @DisplayName("Content 생성 실패 - 다중선택형인데 옵션이 빈 리스트")
    void createContentMultipleTypesIsNull() {
        // given & when & then
        assertThatThrownBy(() -> new Content(
                "취미",
                "취미를 선택해주세요",
                InputType.MULTIPLE_TYPE,
                false,
                2,
                List.of()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("선택형 항목은 선택 옵션이 필요합니다.");
    }
} 