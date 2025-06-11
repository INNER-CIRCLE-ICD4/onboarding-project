package kr.co.fastcampus.onboarding.hyeongminonboarding.global.aop;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import jakarta.persistence.PrePersist;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class SnowflakeIdListener {
    private static final Snowflake SF = IdUtil.getSnowflake(1, 1);

    @PrePersist
    public void onPrePersist(Object entity) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor("id", entity.getClass());
            Method getter = pd.getReadMethod();
            Object current = getter.invoke(entity);
            boolean empty = current == null
                    || (current instanceof Number && ((Number) current).longValue() == 0L);
            if (empty) {
                Method setter = pd.getWriteMethod();
                setter.invoke(entity, SF.nextId());
            }
        } catch (Exception ignored) {

        }
    }
}
