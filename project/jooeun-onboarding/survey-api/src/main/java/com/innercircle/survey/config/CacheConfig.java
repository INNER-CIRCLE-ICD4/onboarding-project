package com.innercircle.survey.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * 캐시 설정
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 캐시 매니저 설정
     * 실제 운영환경에서는 Redis 등의 분산 캐시 사용 권장
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Collections.singleton("survey-statistics"));
        return cacheManager;
    }
}
