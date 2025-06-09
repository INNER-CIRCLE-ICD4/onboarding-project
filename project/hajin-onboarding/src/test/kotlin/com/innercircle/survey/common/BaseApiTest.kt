package com.innercircle.survey.common

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
abstract class BaseApiTest : DescribeSpec() {
    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    override fun extensions() = listOf(SpringExtension)

    init {
        beforeSpec {
            RestAssured.port = port
            RestAssured.basePath = "/api"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }

        afterSpec {
            RestAssured.reset()
        }
    }

    protected fun Any.toJson(): String = objectMapper.writeValueAsString(this)

    protected fun <T> String.fromJson(clazz: Class<T>): T = objectMapper.readValue(this, clazz)
}
