package com.innercircle.survey.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BaseEntity 추상 클래스 테스트")
class BaseEntityTest {

    // 테스트를 위한 구체적인 구현체
    static class TestEntity extends BaseEntity {
        private String name;
        
        public TestEntity(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void callInitializeTimestamps() {
            initializeTimestamps();
        }
        
        public void callOnCreate() {
            onCreate();
        }
        
        public void callOnUpdate() {
            onUpdate();
        }
    }

    @Test
    @DisplayName("initializeTimestamps 메서드가 올바르게 동작한다")
    void shouldInitializeTimestamps() {
        // given
        TestEntity entity = new TestEntity("test");
        
        // when
        entity.callInitializeTimestamps();
        
        // then
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(entity.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("이미 생성시간이 설정된 경우 initializeTimestamps는 생성시간을 변경하지 않는다")
    void shouldNotChangeCreatedAtWhenAlreadySet() {
        // given
        TestEntity entity = new TestEntity("test");
        entity.callInitializeTimestamps();
        LocalDateTime originalCreatedAt = entity.getCreatedAt();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        // when - 약간의 시간이 지난 후 다시 호출
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        entity.callInitializeTimestamps();
        
        // then
        assertThat(entity.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("onCreate 메서드가 올바르게 동작한다")
    void shouldCallOnCreate() {
        // given
        TestEntity entity = new TestEntity("test");
        
        // when
        entity.callOnCreate();
        
        // then
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("onUpdate 메서드가 수정시간을 업데이트한다")
    void shouldUpdateModifiedTime() throws InterruptedException {
        // given
        TestEntity entity = new TestEntity("test");
        entity.callInitializeTimestamps();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();
        
        // when
        Thread.sleep(1); // 시간 차이를 만들기 위해
        entity.callOnUpdate();
        
        // then
        assertThat(entity.getUpdatedAt()).isAfter(originalUpdatedAt);
        assertThat(entity.getUpdatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("기본 version 값이 0으로 설정된다")
    void shouldHaveDefaultVersionZero() {
        // given & when
        TestEntity entity = new TestEntity("test");
        
        // then
        assertThat(entity.getVersion()).isEqualTo(0L);
    }

    @Test
    @DisplayName("엔티티의 모든 getter가 올바르게 동작한다")
    void shouldGetAllProperties() {
        // given
        TestEntity entity = new TestEntity("test");
        entity.callInitializeTimestamps();
        
        // when & then
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getVersion()).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
    }
}
