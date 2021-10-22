package com.example.kotlinspringboottest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class MemberRouter(private val handler: FirstHandler) {

    @Bean
    fun routerFunction() = nest(path("/member"),
            router {
                listOf(
                    POST("/", handler::getAll),
                    GET("/{id}", handler::getById),
                    POST("/save", handler::save),
                    PUT("/{id}/done", handler::done),
                    DELETE("/{id}", handler::delete))
            }
        )
}