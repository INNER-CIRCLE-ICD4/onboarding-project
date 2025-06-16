package kr.innercircle.onboarding.survey.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * packageName : kr.innercircle.onboarding.survey.controller
 * fileName    : TestController
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@RestController
class TestController {

    @GetMapping("/test")
    fun helloWorld(): String {
        return "Hello, World!"
    }
}