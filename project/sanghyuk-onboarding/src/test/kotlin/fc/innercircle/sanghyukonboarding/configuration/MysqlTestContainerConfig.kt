package fc.innercircle.sanghyukonboarding.configuration

import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.time.Duration
import kotlin.apply
import kotlin.text.isNullOrBlank

/**
 * Shared TestContainer configuration for all tests.
 * This class ensures that only one MySQL container is started for all tests,
 * improving test execution time and resource usage.
 */
@ActiveProfiles("test")
@TestConfiguration
class MysqlTestContainerConfig {

    companion object {
        // Static container instance that will be shared across all tests
        private val mysqlContainer: MySQLContainer<Nothing> by lazy {
            MySQLContainer<Nothing>("mysql:8.0.32").apply {
                withDatabaseName("iems")
                withUsername("aerix")
                withPassword("!aerix123")
                withUrlParam("useSSL", "false")
                withUrlParam("allowPublicKeyRetrieval", "true")
                withUrlParam("characterEncoding", "UTF-8")
                withUrlParam("serverTimezone", "Asia/Seoul")
                // Increase connection timeout
                withConnectTimeoutSeconds(60)
                // Wait for MySQL to be ready
                waitingFor(Wait.forLogMessage(".*ready for connections.*", 2))
                withStartupTimeout(Duration.ofMinutes(2))
                start()
            }
        }

        init {
            // Set Docker host for CI/CD environment if needed
            val dockerHost = System.getenv("TESTCONTAINERS_HOST_OVERRIDE")
            if (!dockerHost.isNullOrBlank()) {
                System.setProperty("testcontainers.docker.host", "tcp://$dockerHost:2375")
            }

            // Initialize the container early to ensure it's ready when tests start
            try {
                mysqlContainer
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Configure system properties immediately
            System.setProperty("spring.datasource.url", getJdbcUrl())
            System.setProperty("spring.datasource.username", getUsername())
            System.setProperty("spring.datasource.password", getPassword())
        }

        /**
         * Get the JDBC URL for the MySQL container
         * @return JDBC URL with additional parameters
         */
        fun getJdbcUrl(): String {
            return mysqlContainer.jdbcUrl
        }

        /**
         * Get the username for the MySQL container
         * @return Username
         */
        fun getUsername(): String {
            return mysqlContainer.username
        }

        /**
         * Get the password for the MySQL container
         * @return Password
         */
        fun getPassword(): String {
            return mysqlContainer.password
        }

        @PreDestroy
        fun stopContainer() {
            if (mysqlContainer.isRunning) {
                mysqlContainer.stop()
            }
        }
    }
}
