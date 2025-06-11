package kr.co.fastcampus.onboarding.hyeongminonboarding.global.aop;


import cn.hutool.core.lang.Snowflake;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

@Aspect
@Component
public class SnowflakeIdAspect {
    private final Snowflake snowflake;

    public SnowflakeIdAspect(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    /**
     * Spring Data 의 save(...) 메서드 진입 전 호출
     * (CrudRepository, JpaRepository 모두 잡히도록 설정)
     */
//    @Before("execution(* org.springframework.data.repository.CrudRepository+.save(..))")
//    @Before("execution(* org.springframework.data.repository.Repository+.*save*(..))")
    @Before("execution(* org.springframework.data.repository.Repository+.*save*(..))")
    public void beforeSave(JoinPoint jp) {
        Object entity = jp.getArgs()[0];
        if (entity == null) {
            return;
        }

        try {
            // 엔티티에 'id' 프로퍼티가 있는지 확인
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(entity.getClass(), "id");
            if (pd == null) {
                return;
            }

            Method getter = pd.getReadMethod();
            Method setter = pd.getWriteMethod();
            if (getter == null || setter == null) {
                return;
            }

            Object current = getter.invoke(entity);
            // 신규 엔티티라면 id == null 또는 0 으로 간주
            boolean isEmpty =
                    current == null
                            || (current instanceof Number && ((Number) current).longValue() == 0L);

            if (isEmpty) {
                long generated = snowflake.nextId();
                setter.invoke(entity, generated);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

}
