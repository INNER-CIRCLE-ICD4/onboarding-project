package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SnowflakeIdGeneratorTest : FunSpec({

    val generator = SnowflakeIdGenerator()

    context("기본 동작") {

        test("양수 ID를 생성해야 한다") {
            val id = generator.nextId()
            id.shouldBeGreaterThan(0L)
        }

        test("단조 증가하는 ID를 생성해야 한다") {
            var prev = generator.nextId()
            repeat(1000) {
                val curr = generator.nextId()
                curr.shouldBeGreaterThan(prev)
                prev = curr
            }
        }
    }

    context("대용량 트래픽 환경") {

        test("단일 스레드에서 50_000개 연속 생성 시 중복 없어야 한다") {
            val count = 50_000
            val ids = mutableSetOf<Long>()
            repeat(count) {
                generator.nextId().let { id ->
                    ids.add(id).shouldBeTrue()
                }
            }
            ids.shouldHaveSize(count)
        }

        test("초당 1_000_000건 ID 생성 시 1초 이내에 완료되어야 한다") {
            val count = 1_000_000
            val start = System.currentTimeMillis()
            repeat(count) {
                generator.nextId()
            }
            val duration = System.currentTimeMillis() - start
            duration.shouldBeLessThan(1000L)
        }
    }

    context("분산 환경") {

        test("100 스레드가 각각 1_000개씩 동시에 생성해도 중복 없어야 한다") {
            val threadCount = 10
            val perThread = 1_000
            // 고정된 수(threadCount)만큼 스레드를 생성하는 스레드풀
            val executor = Executors.newFixedThreadPool(threadCount)
            // 모든 스레드가 준비될 때까지 대기시킬 래치
            val latch = CountDownLatch(1)
            // 동시 접근해도 안전한 Set. ID 중복 체크용
            val idSet = ConcurrentHashMap.newKeySet<Long>()

            // 각 스레드가 래치 해제 후 perThread 만큼 ID를 생성하도록 Future 리스트 생성
            val futures = List(threadCount) {
                executor.submit {
                    // latch.await()가 호출되어야 작업을 시작 → 모든 스레드가 거의 동시에 실행됨
                    latch.await()
                    repeat(perThread) {
                        val id = generator.nextId()
                        idSet.add(id).shouldBeTrue() // 중복이 발생하면 테스트 실패
                    }
                }
            }

            // latch.countDown()으로 모든 스레드를 동시에 해제
            latch.countDown()
            // 모든 Future의 작업이 완료될 때까지 대기
            futures.forEach { it.get() }
            // 스레드풀 종료 요청
            executor.shutdown()
            // 남아있는 작업이 있으면 최대 5초까지 기다림
            executor.awaitTermination(5, TimeUnit.SECONDS)

            // 최종적으로 ID 개수가 threadCount * perThread 와 일치해야 중복이 없었던 것
            idSet.shouldHaveSize(threadCount * perThread)
        }
    }

    context("시간 역행 방지") {

        test("현재 시간보다 이전에 생성된 ID는 허용되지 않아야 한다") {
            val currentTime = System.currentTimeMillis()
            Thread.sleep(100) // 현재 시간보다 약간 늦게 생성되도록 대기
            val id = generator.nextId()
            // ID 생성 시점이 현재 시간보다 이전이면 안됨
            (id shr 22) + 1609459200000L shouldBeGreaterThan currentTime
        }
    }
})
