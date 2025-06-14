package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler;

public interface Assembler<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
