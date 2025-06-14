package com.example.hyeongwononboarding.common.util;

import com.github.f4b6a3.uuid.UuidCreator;

/**
 * UUID 생성 유틸리티
 * - UUID v7을 사용하여 시간 기반 정렬 가능한 UUID 생성
 */
public class UUIDGenerator {
    /**
     * UUID v7(문자열) 생성
     * @return 시간 기반 정렬 가능한 UUID 문자열
     */
    public static String generate() {
        return UuidCreator.getTimeOrderedWithHash().toString();
    }
}
