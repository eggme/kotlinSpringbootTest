package com.example.kotlinspringboottest

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@Component
class FirstHandler(val mbrepo: MemberRepository){

    fun getAll(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(mbrepo.findAll()))
        .switchIfEmpty(notFound().build())

    fun getById(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.justOrEmpty(mbrepo.findById(request.pathVariable("id").toInt())))
        .switchIfEmpty(notFound().build())

    fun save(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(request.bodyToMono(Members::class.java)
            .switchIfEmpty(Mono.empty())
            .filter(Objects::isNull)
            .flatMap { members ->
                Mono.fromCallable {
                    println("save Members")
                    mbrepo.save(members)
                }.then(Mono.just(members))
            }).switchIfEmpty(notFound().build())

    fun done(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.justOrEmpty(mbrepo.findById(request.pathVariable("id").toInt()))
            .switchIfEmpty(Mono.empty())
            .filter(Objects::nonNull)
            .flatMap { members ->
                Mono.fromCallable {
                    members.done = true
                    members.modifiedAt = LocalDateTime.now()
                    mbrepo.save(members)
                }.then(Mono.just(members))
            }).switchIfEmpty(notFound().build())

    fun delete(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.justOrEmpty(mbrepo.findById(request.pathVariable("id").toInt()))
            .switchIfEmpty(Mono.empty())
            .filter(Objects::nonNull)
            .flatMap { members ->
                Mono.fromCallable {
                    mbrepo.delete(members)
                }.then(Mono.just(members))
            }).switchIfEmpty(notFound().build())
}