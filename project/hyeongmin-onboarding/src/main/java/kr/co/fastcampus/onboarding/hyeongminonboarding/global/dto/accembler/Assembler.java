package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler;

import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblyContext;

public interface Assembler<D> {
    D assemble(AssemblyContext context);
    Class<D> supportType();
}