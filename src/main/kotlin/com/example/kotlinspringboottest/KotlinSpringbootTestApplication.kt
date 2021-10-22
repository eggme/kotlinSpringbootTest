package com.example.kotlinspringboottest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinSpringbootTestApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringbootTestApplication>(*args)
}
