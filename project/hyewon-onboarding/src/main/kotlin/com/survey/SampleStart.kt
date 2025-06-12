package com.survey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SampleStartApplication

fun main(args: Array<String>) {
    runApplication<SampleStartApplication>(*args)
}

@RestController
class SampleStart {
    @GetMapping("/hello")
    fun hello(): String = "Hello"
}