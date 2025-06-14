package kr.co.fastcampus.onboarding.hyeongminonboarding.global.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class SnowflakeConfig {
    /**
     * workerId, datacenterId 는 환경별로 다르게 설정하셔도 됩니다.
     */
    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(1, 1);
    }
}