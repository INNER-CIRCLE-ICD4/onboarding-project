package kr.co.fastcampus.onboarding.hyeongminonboarding.global.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer javaTimeCustomizer() {
        return builder -> {
            // 타임스탬프로 나가는 걸 막고(WRITE_DATES_AS_TIMESTAMPS = false)
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // JavaTimeModule 에 Serializer/Deserializer 추가
            JavaTimeModule module = new JavaTimeModule();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(fmt));
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(fmt));

            builder.modules(module);
        };
    }
}