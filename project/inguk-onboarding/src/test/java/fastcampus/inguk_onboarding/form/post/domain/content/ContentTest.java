package fastcampus.inguk_onboarding.form.post.domain.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ContentTest {

    @Test
    @DisplayName("정상적인 Content 생성")
    void createContent_정상() {
        // given & when
        Content content = new Content("설문조사", "설문조사 설명");

        // then
        assertThat(content.getName()).isEqualTo("설문조사");
        assertThat(content.getDescription()).isEqualTo("설문조사 설명");
    }

    @Test
    @DisplayName("Content 생성 실패 - 이름이 null")
    void createContent_실패_이름null() {
        // given & when & then
        assertThatThrownBy(() -> new Content(null, "설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("Content 생성 실패 - 이름이 빈 문자열")
    void createContent_실패_이름빈문자열() {
        // given & when & then
        assertThatThrownBy(() -> new Content("   ", "설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("Content 생성 성공 - 설명이 null")
    void createContent_성공_설명null() {
        // given & when
        Content content = new Content("이름", null);

        // then
        assertThat(content.getName()).isEqualTo("이름");
        assertThat(content.getDescription()).isNull();
    }

    @Test
    @DisplayName("Content equals와 hashCode 정상 동작")
    void content_equals_hashCode() {
        // given
        Content content1 = new Content("이름", "설명");
        Content content2 = new Content("이름", "설명");
        Content content3 = new Content("다른이름", "설명");
        Content content4 = new Content("이름", "다른설명");

        // when & then
        assertThat(content1).isEqualTo(content2);
        assertThat(content1).isNotEqualTo(content3);
        assertThat(content1).isNotEqualTo(content4);
        assertThat(content1.hashCode()).isEqualTo(content2.hashCode());
    }

    @Test
    @DisplayName("Content toString 정상 동작")
    void content_toString() {
        // given
        Content content = new Content("이름", "설명");

        // when
        String result = content.toString();

        // then
        assertThat(result).contains("이름");
        assertThat(result).contains("설명");
        assertThat(result).contains("Content");
    }
} 