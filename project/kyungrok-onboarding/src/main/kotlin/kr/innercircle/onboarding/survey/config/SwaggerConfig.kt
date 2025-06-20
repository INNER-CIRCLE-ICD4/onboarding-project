package kr.innercircle.onboarding.survey.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * packageName : kr.innercircle.onboarding.survey.config
 * fileName    : SwaggerConfig
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
@Configuration
@OpenAPIDefinition(
    info = Info(title = "survey service", description = "", version = "1.0.0"),
    servers = [Server(url = "\${server.servlet.context-path}")]
)
class SwaggerConfig{
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
    }
}