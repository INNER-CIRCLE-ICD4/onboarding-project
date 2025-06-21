package com.innercircle.survey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SurveyApplication 테스트")
class SurveyApplicationTest {

    @Test
    @DisplayName("Spring Boot 애플리케이션 컨텍스트가 정상적으로 로드된다")
    void contextLoads() {
        // Spring Boot 애플리케이션 컨텍스트가 정상적으로 로드되는지 확인
    }

    @Test
    @DisplayName("기본 생성자로 SurveyApplication 인스턴스를 생성할 수 있다")
    void shouldCreateSurveyApplicationInstance() {
        // when & then
        assertThatCode(() -> {
            SurveyApplication application = new SurveyApplication();
            assertThat(application).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("SurveyApplication 클래스가 올바른 애노테이션을 가진다")
    void shouldHaveCorrectAnnotations() {
        // when & then
        org.springframework.boot.autoconfigure.SpringBootApplication annotation = 
            SurveyApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
        
        // SpringBootApplication 애노테이션이 존재하는지 확인
        assertThat(annotation).isNotNull();
    }

    @Test
    @DisplayName("main 메서드가 존재한다")
    void shouldHaveMainMethod() throws NoSuchMethodException {
        // when & then
        var mainMethod = SurveyApplication.class.getMethod("main", String[].class);
        assertThat(mainMethod).isNotNull();
        assertThat(mainMethod.getReturnType()).isEqualTo(void.class);
        
        // main 메서드가 static인지 확인
        assertThat(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers())).isTrue();
        assertThat(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("SurveyApplication이 스프링 부트 애플리케이션 클래스로 올바르게 설정되어 있다")
    void shouldBeValidSpringBootApplication() {
        // when & then
        assertThat(SurveyApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class)).isTrue();
        
        // 패키지 구조 확인
        assertThat(SurveyApplication.class.getPackage().getName()).isEqualTo("com.innercircle.survey");
    }
}
