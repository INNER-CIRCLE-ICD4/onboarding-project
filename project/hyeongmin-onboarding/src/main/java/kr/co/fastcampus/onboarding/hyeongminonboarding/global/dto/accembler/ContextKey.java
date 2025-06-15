package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler;

import lombok.Getter;

@Getter
public class ContextKey<T> {
    private final String name;
    private final Class<?> type;

    public ContextKey(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

}