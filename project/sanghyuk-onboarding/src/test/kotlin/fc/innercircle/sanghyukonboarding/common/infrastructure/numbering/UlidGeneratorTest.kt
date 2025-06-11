package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * UlidGenerator의 대용량 트래픽, 동시성, 분산 환경에서의 안전성을 검증하는 테스트
 *
 * ULID 구조:
 * - 26자리 문자열 (Crockford's Base32 알파벳 사용: 0123456789ABCDEFGHJKMNPQRSTVWXYZ)
 * - 첫 10자리: 타임스탬프 (밀리초)
 * - 나머지 16자리: 무작위 값
 */
class UlidGeneratorTest : FunSpec({

    val generator = UlidGenerator()

    context("기본 동작") {
        test("ULID 형식에 맞는 ID를 생성해야 한다") {
            val id = generator.nextId()
            // ULID는 26자리 문자열이어야 함
            id.shouldHaveLength(26)
            // ULID는 Crockford's Base32 알파벳만 사용해야 함 (0-9, A-Z, 제외: I, L, O, U)
            id.shouldMatch("^[0-9A-HJKMNP-TV-Z]{26}$")
        }

        test("연속 생성된 ID는 모두 고유해야 한다") {
            val count = 1000
            val ids = mutableSetOf<String>()
            repeat(count) {
                generator.nextId().let { id ->
                    ids.add(id).shouldBeTrue()
                }
            }
            ids.shouldHaveSize(count)
        }

        test("시간 순서가 보존되어야 한다") {
            // 첫 번째 ID 생성
            val id1 = generator.nextId()
            // 약간의 시간 간격을 두고 두 번째 ID 생성
            Thread.sleep(5)
            val id2 = generator.nextId()

            // ULID의 첫 10자리는 타임스탬프
            val timestamp1 = id1.substring(0, 10)
            val timestamp2 = id2.substring(0, 10)

            // 두 번째 ID의 타임스탬프는 첫 번째보다 크거나 같아야 함
            (timestamp2 >= timestamp1).shouldBeTrue()
        }
    }

    context("대용량 트래픽 환경") {
        test("단일 스레드에서 50,000개 연속 생성 시 중복 없어야 한다") {
            val count = 50_000
            val ids = mutableSetOf<String>()
            repeat(count) {
                generator.nextId().let { id ->
                    ids.add(id).shouldBeTrue()
                }
            }
            ids.shouldHaveSize(count)
        }

        test("초당 100,000건 ID 생성 시 1초 이내에 완료되어야 한다") {
            val count = 100_000
            val start = System.currentTimeMillis()
            repeat(count) {
                generator.nextId()
            }
            val duration = System.currentTimeMillis() - start

            // 100,000개 ID 생성이 1초 이내에 완료되어야 함
            println("[DEBUG_LOG] 100,000개 ID 생성 소요 시간: $duration ms")
            (duration < 1000).shouldBeTrue()
        }
    }

    context("동시성이 매우 많은 환경") {
        test("100 스레드가 각각 1,000개씩 동시에 생성해도 중복 없어야 한다") {
            val threadCount = 100
            val perThread = 1_000
            // 고정된 수(threadCount)만큼 스레드를 생성하는 스레드풀
            val executor = Executors.newFixedThreadPool(threadCount)
            // 모든 스레드가 준비될 때까지 대기시킬 래치
            val latch = CountDownLatch(1)
            // 동시 접근해도 안전한 Set. ID 중복 체크용
            val idSet = ConcurrentHashMap.newKeySet<String>()

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

    context("분산 환경") {
        test("여러 노드에서 생성된 ID도 고유해야 한다 (시뮬레이션)") {
            // 여러 노드를 시뮬레이션하기 위해 여러 UlidGenerator 인스턴스 생성
            val nodeCount = 10
            val generators = List(nodeCount) { UlidGenerator() }
            val perNode = 1_000

            // 각 노드(제너레이터)에서 생성한 ID를 모두 저장
            val allIds = mutableSetOf<String>()

            // 각 노드에서 ID 생성
            generators.forEach { nodeGenerator ->
                repeat(perNode) {
                    val id = nodeGenerator.nextId()
                    allIds.add(id).shouldBeTrue() // 중복이 발생하면 테스트 실패
                }
            }

            // 모든 노드에서 생성된 ID의 총 개수가 nodeCount * perNode와 일치해야 함
            allIds.shouldHaveSize(nodeCount * perNode)
        }

        test("분산 환경에서 동시에 ID를 생성해도 중복이 없어야 한다 (시뮬레이션)") {
            val nodeCount = 10
            val threadPerNode = 10
            val perThread = 100

            // 각 노드를 시뮬레이션하기 위한 제너레이터 목록
            val generators = List(nodeCount) { UlidGenerator() }
            // 모든 ID를 저장할 집합
            val allIds = ConcurrentHashMap.newKeySet<String>()
            // 모든 스레드가 동시에 시작하도록 하는 래치
            val latch = CountDownLatch(1)

            // 각 노드마다 스레드풀 생성
            val executors = generators.map { Executors.newFixedThreadPool(threadPerNode) }

            // 모든 노드의 모든 스레드에서 작업 제출
            val allFutures = executors.flatMap { executor ->
                val nodeGenerator = generators[executors.indexOf(executor)]
                List(threadPerNode) {
                    executor.submit {
                        latch.await() // 모든 스레드가 준비될 때까지 대기
                        repeat(perThread) {
                            val id = nodeGenerator.nextId()
                            allIds.add(id).shouldBeTrue() // 중복 체크
                        }
                    }
                }
            }

            // 모든 스레드 동시에 시작
            latch.countDown()

            // 모든 작업 완료 대기
            allFutures.forEach { it.get() }

            // 스레드풀 종료
            executors.forEach {
                it.shutdown()
                it.awaitTermination(5, TimeUnit.SECONDS)
            }

            // 총 ID 개수 확인
            val expectedCount = nodeCount * threadPerNode * perThread
            allIds.shouldHaveSize(expectedCount)
        }
    }
})
