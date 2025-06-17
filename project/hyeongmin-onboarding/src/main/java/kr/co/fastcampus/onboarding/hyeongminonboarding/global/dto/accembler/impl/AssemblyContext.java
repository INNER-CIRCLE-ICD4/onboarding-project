package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl;

import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.ContextKey;

import java.util.HashMap;
import java.util.Map;

public class AssemblyContext {
    private final Map<String, Object> map = new HashMap<>();

    public <T> void put(ContextKey<T> key, T value) {
        map.put(key.getName(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ContextKey<T> key) {
        return (T) map.get(key.getName());
    }
}
