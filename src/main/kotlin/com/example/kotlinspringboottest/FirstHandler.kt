package com.example.kotlinspringboottest

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Component
@Transactional
class FirstHandler{

    private lateinit var mbrepo: MemberRepository

    fun test(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(listOf(1,2,3,4,5).map {
            if(it == 1) {
                val member = mbrepo.save(Members(1).apply {
                    this.name = "테스트"
                    this.age = 25
                })
                println("member(mbno=${member.mbno}, name=${member.name}, age=${member.age}, createAt=${member.createdAt})")
            }
            println("response = $it")
            it
        })
        )

    fun getAll(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(mbrepo.findAll()))
        .switchIfEmpty(notFound().build())

    fun getById(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.justOrEmpty(mbrepo.findById(request.pathVariable("id").toInt())))
        .switchIfEmpty(notFound().build())

    fun saveTest(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(request.bodyToMono(Members::class.java))
        .switchIfEmpty(Mono.empty())


    fun save(request: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
//        .body(request.bodyToMono(Members::class.java))
//        .switchIfEmpty(Mono.empty())
        .body(request.bodyToMono(Members::class.java)
            .map{
                println("member(mbno=${it.mbno}, name=${it.name}, age=${it.age}, createAt=${it.createdAt})")
                mbrepo.save(it)
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