package com.innercircle.survey.common.util;

import de.huxhorn.sulky.ulid.ULID;

/**
 * ULID (Universally Unique Lexicographically Sortable Identifier) 생성 유틸리티
 * 
 * 분산 환경에서 고유하고 시간순 정렬이 가능한 ID를 생성합니다.
 * - UUID 대비 시간 순서 보장
 * - 데이터베이스 인덱스 성능 향상
 * - 다중 인스턴스에서 충돌 없는 ID 생성
 */
public final class UlidGenerator {

    private static final ULID ulid = new ULID();

    private UlidGenerator() {
        throw new AssertionError("인스턴스화할 수 없습니다.");
    }

    /**
     * 새로운 ULID 문자열을 생성합니다.
     *
     * @return 26자리 ULID 문자열 (예: 01ARZ3NDEKTSV4RRFFQ69G5FAV)
     */
    public static String generate() {
        return ulid.nextULID();
    }

    /**
     * 지정된 타임스탬프로 ULID를 생성합니다.
     *
     * @param timestamp 밀리초 단위 타임스탬프
     * @return ULID 문자열
     */
    public static String generate(long timestamp) {
        return ulid.nextULID(timestamp);
    }

    /**
     * ULID에서 타임스탬프를 추출합니다.
     *
     * @param ulidString ULID 문자열
     * @return 밀리초 단위 타임스탬프
     */
    public static long extractTimestamp(String ulidString) {
        return ULID.parseULID(ulidString).timestamp();
    }
}
