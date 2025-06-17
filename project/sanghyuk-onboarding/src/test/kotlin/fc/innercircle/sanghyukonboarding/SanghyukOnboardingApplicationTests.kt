package fc.innercircle.sanghyukonboarding

import fc.innercircle.sanghyukonboarding.configuration.MysqlTestContainerConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(MysqlTestContainerConfig::class)
@SpringBootTest
class SanghyukOnboardingApplicationTests {
    @Test
    fun contextLoads() {
    }
}
