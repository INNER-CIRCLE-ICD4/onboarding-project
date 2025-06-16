package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl;

import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.Assembler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@Component
public class AssemblerFactory {
    private final Map<Class<?>, Assembler<?>> assemblerMap = new HashMap<>();

    public AssemblerFactory(List<Assembler<?>> assemblers) {
        for (Assembler<?> assembler : assemblers) {
            assemblerMap.put(assembler.supportType(), assembler);
        }
    }

    @SuppressWarnings("unchecked")
    public <D> D assemble(Class<D> dtoType, Consumer<AssemblyContext> contextBuilder) {
        Assembler<D> assembler = (Assembler<D>) assemblerMap.get(dtoType);
        if (assembler == null) {
            throw new IllegalArgumentException("찾을 수 없는 Context : " + dtoType);
        }

        AssemblyContext context = new AssemblyContext();
        contextBuilder.accept(context);
        return assembler.assemble(context);
    }
}
