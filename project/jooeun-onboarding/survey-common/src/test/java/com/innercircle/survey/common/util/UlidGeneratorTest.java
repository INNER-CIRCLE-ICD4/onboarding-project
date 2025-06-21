package com.innercircle.survey.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UlidGenerator 유틸리티 테스트")
class UlidGeneratorTest {

    @Test
    @DisplayName("인스턴스화할 수 없다")
    void shouldNotBeInstantiable() {
        // when & then
        assertThatThrownBy(() -> {
            var constructor = UlidGenerator.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }).getCause().isInstanceOf(AssertionError.class)
          .hasMessage("인스턴스화할 수 없습니다.");
    }

    @Test
    @DisplayName("ULID를 생성할 수 있다")
    void shouldGenerateUlid() {
        // when
        String ulid = UlidGenerator.generate();

        // then
        assertThat(ulid).isNotNull();
        assertThat(ulid).hasSize(26);
        assertThat(ulid).matches("^[0-9A-HJKMNP-TV-Z]{26}$"); // Crockford Base32
    }

    @RepeatedTest(100)
    @DisplayName("생성되는 ULID는 고유하다")
    void shouldGenerateUniqueUlids() {
        // given
        Set<String> ulids = ConcurrentHashMap.newKeySet();

        // when
        for (int i = 0; i < 1000; i++) {
            String ulid = UlidGenerator.generate();
            boolean isUnique = ulids.add(ulid);
            
            // then
            assertThat(isUnique).isTrue();
        }
    }

    @Test
    @DisplayName("동일한 타임스탬프로 생성된 ULID도 고유하다")
    void shouldGenerateUniqueUlidsWithSameTimestamp() {
        // given
        long timestamp = System.currentTimeMillis();
        Set<String> ulids = ConcurrentHashMap.newKeySet();

        // when
        for (int i = 0; i < 100; i++) {
            String ulid = UlidGenerator.generate(timestamp);
            boolean isUnique = ulids.add(ulid);
            
            // then
            assertThat(isUnique).isTrue();
        }
    }

    @Test
    @DisplayName("타임스탬프를 지정하여 ULID를 생성할 수 있다")
    void shouldGenerateUlidWithTimestamp() {
        // given
        long specificTimestamp = 1609459200000L; // 2021-01-01 00:00:00 UTC

        // when
        String ulid = UlidGenerator.generate(specificTimestamp);

        // then
        assertThat(ulid).isNotNull();
        assertThat(ulid).hasSize(26);
        
        // 타임스탬프 추출하여 검증
        long extractedTimestamp = UlidGenerator.extractTimestamp(ulid);
        assertThat(extractedTimestamp).isEqualTo(specificTimestamp);
    }

    @Test
    @DisplayName("ULID에서 타임스탬프를 추출할 수 있다")
    void shouldExtractTimestampFromUlid() {
        // given
        long beforeGeneration = System.currentTimeMillis();
        String ulid = UlidGenerator.generate();
        long afterGeneration = System.currentTimeMillis();

        // when
        long extractedTimestamp = UlidGenerator.extractTimestamp(ulid);

        // then
        assertThat(extractedTimestamp).isBetween(beforeGeneration, afterGeneration);
    }

    @Test
    @DisplayName("시간 순서대로 ULID가 생성된다")
    void shouldGenerateUlidsInTimeOrder() throws InterruptedException {
        // given
        String firstUlid = UlidGenerator.generate();
        Thread.sleep(2); // 최소한의 시간 차이 보장
        String secondUlid = UlidGenerator.generate();

        // when
        long firstTimestamp = UlidGenerator.extractTimestamp(firstUlid);
        long secondTimestamp = UlidGenerator.extractTimestamp(secondUlid);

        // then
        assertThat(firstTimestamp).isLessThanOrEqualTo(secondTimestamp);
        assertThat(firstUlid.compareTo(secondUlid)).isLessThan(0); // 사전식 순서도 유지
    }

    @Test
    @DisplayName("멀티스레드 환경에서 고유한 ULID를 생성한다")
    void shouldGenerateUniqueUlidsInMultiThreadedEnvironment() throws InterruptedException {
        // given
        int threadCount = 10;
        int ulidsPerThread = 100;
        Set<String> allUlids = ConcurrentHashMap.newKeySet();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ulidsPerThread; j++) {
                        String ulid = UlidGenerator.generate();
                        allUlids.add(ulid);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then
        assertThat(allUlids).hasSize(threadCount * ulidsPerThread);
    }

    @Test
    @DisplayName("과거 타임스탬프로 ULID를 생성할 수 있다")
    void shouldGenerateUlidWithPastTimestamp() {
        // given
        long pastTimestamp = Instant.parse("2020-01-01T00:00:00Z").toEpochMilli();

        // when
        String ulid = UlidGenerator.generate(pastTimestamp);
        long extractedTimestamp = UlidGenerator.extractTimestamp(ulid);

        // then
        assertThat(extractedTimestamp).isEqualTo(pastTimestamp);
    }

    @Test
    @DisplayName("미래 타임스탬프로 ULID를 생성할 수 있다")
    void shouldGenerateUlidWithFutureTimestamp() {
        // given
        long futureTimestamp = Instant.parse("2030-01-01T00:00:00Z").toEpochMilli();

        // when
        String ulid = UlidGenerator.generate(futureTimestamp);
        long extractedTimestamp = UlidGenerator.extractTimestamp(ulid);

        // then
        assertThat(extractedTimestamp).isEqualTo(futureTimestamp);
    }

    @Test
    @DisplayName("0 타임스탬프로 ULID를 생성할 수 있다")
    void shouldGenerateUlidWithZeroTimestamp() {
        // given
        long zeroTimestamp = 0L;

        // when
        String ulid = UlidGenerator.generate(zeroTimestamp);
        long extractedTimestamp = UlidGenerator.extractTimestamp(ulid);

        // then
        assertThat(extractedTimestamp).isEqualTo(zeroTimestamp);
    }
}
