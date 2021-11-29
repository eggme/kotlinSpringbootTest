package com.example.kotlinspringboottest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.router

@Configuration
class MemberRouter(private val handler: FirstHandler) {

    // Rebase Test 1
    @Bean
    fun routerFunction() = router {
        "/test".nest {
            GET("/", handler::test)
            POST("/api", handler::saveTest)
        }
        "/member".nest {
            listOf(
                POST("/", handler::getAll),
                GET("/{id}", handler::getById),
                POST("/save", handler::save),
                PUT("/{id}/done", handler::done),
                DELETE("/{id}", handler::delete)
            )
        }
    }
}