
package survey.common.snowflake;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnowflakeTest {
    private Snowflake snowflake;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        snowflake = new Snowflake();
        executorService = Executors.newFixedThreadPool(10);
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("ExecutorService did not terminate");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @DisplayName("Snowflake ID는 분산 환경에서도 중복되지 않고 순차적으로 증가해야 한다.")
    @Test
    void nextIdTest() throws ExecutionException, InterruptedException {
        // given
        List<Future<List<Long>>> futures = new ArrayList<>();
        int repeatCount = 1000;
        int idCount = 1000;

        // when
        for (int i = 0; i < repeatCount; i++) {
            futures.add(executorService.submit(() -> generateIdList(snowflake, idCount)));
        }

        // then
        List<Long> result = new ArrayList<>();
        for (Future<List<Long>> future : futures) {
            List<Long> idList = future.get();
            for (int i = 1; i < idList.size(); i++) {
                assertThat(idList.get(i)).isGreaterThan(idList.get(i - 1));
            }
            result.addAll(idList);
        }

        Set<Long> uniqueIds = new HashSet<>(result);
        assertThat(uniqueIds.size()).isEqualTo(repeatCount * idCount);
    }

    List<Long> generateIdList(Snowflake snowflake, int count) {
        List<Long> idList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            idList.add(snowflake.nextId());
        }
        return idList;
    }

    @DisplayName("Snowflake ID는 대규모 트래픽에서도 요구하는 성능을 만족해야 한다.")
    @Test
    @Timeout(value = 10)
    void nextIdPerformanceTest() throws InterruptedException {
        // given
        int repeatCount = 1000;
        int idCount = 1000;
        int requiredPerformanceTime = 5000;
        CountDownLatch latch = new CountDownLatch(repeatCount);

        // when
        long start = System.nanoTime();
        for (int i = 0; i < repeatCount; i++) {
            executorService.submit(() -> {
                try {
                    generateIdList(snowflake, idCount);
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);

        long end = System.nanoTime();
        long elapsedTimeMs = (end - start) / 1_000_000;
        System.out.printf("생성 시간 = %s ms%n", elapsedTimeMs);

        // then
        assertTrue(completed, "작업이 시간 내에 완료되지 않았습니다");
        assertThat(elapsedTimeMs).isLessThan(requiredPerformanceTime);
    }

    @Test
    void differentWorkerIdTest() {
        // given
        Snowflake snowflake1 = new Snowflake();
        Snowflake snowflake2 = new Snowflake();

        // when
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            ids.add(snowflake1.nextId());
            ids.add(snowflake2.nextId());
        }

        // then
        assertThat(ids.size()).isEqualTo(200);
    }

    @Test
    void clockBackwardsTest() {
        // given
        ClockBackwardsMockSnowflake mockSnowflake = new ClockBackwardsMockSnowflake();

        mockSnowflake.nextId();

        // then
        assertThatThrownBy(mockSnowflake::nextId)
                .isInstanceOf(Snowflake.ClockMovedBackwardsException.class)
                .hasMessageContaining("시계가 역행했습니다");
    }


    private static class ClockBackwardsMockSnowflake extends Snowflake {
        private boolean firstCall = true;

        @Override
        protected long timestamp() {
            if (firstCall) {
                firstCall = false;
                return System.currentTimeMillis();
            } else {
                return System.currentTimeMillis() - 1000;
            }
        }
    }
}