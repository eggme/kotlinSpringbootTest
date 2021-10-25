package com.example.kotlinspringboottest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.function.BiFunction
import javax.transaction.Transactional

@RestController
@Transactional
class TestController {

    val basket1 = listOf("kiwi", "orange", "lemon", "orange", "lemon", "kiwi")
    val basket2 = listOf("banana", "lemon", "lemon", "kiwi")
    val basket3 = listOf("strawberry", "orange", "lemon", "grape", "strawberry")

    val baskets = listOf(basket1, basket2, basket3)
    val basketFlux = Flux.fromIterable(baskets)
    @Autowired private lateinit var memberRepository: MemberRepository

    /**
    Mono -> 0~1개의 결과만을 처리하기 위한 Reactor의 객체
    Flux -> 0~N개의 결과를 처리하는 객체
     */

    @GetMapping(value = ["/testRest"])
    fun test(): ResponseEntity<Any> {
        var member = Members(1).apply {
            this.name = "관리자"
            this.age = 27
        }
        memberRepository.save(member)
        println(member)

        val resultMap = basketFlux.concatMap {
            val collectList = Flux.fromIterable(it).distinct().collectList()
            val groupItem = Flux.fromIterable(it).groupBy { fruit -> fruit }.concatMap { p ->
                p.count().map { length ->
                    {
                        val map = linkedMapOf<String, Long>()
                        map.put(p.key(), length)
                        map
                    }
                }
            }.reduce { oldMap, currentMap ->
                {
                    linkedMapOf<String, Long>().apply {
                        this.putAll(oldMap.invoke())
                        this.putAll(currentMap.invoke())
                    }
                }
            }
            Flux.zip(collectList, groupItem) { distinct, countMap -> FruitInfo(distinct, countMap.invoke()) }
        }.subscribe{
            println(it)
        }

        return ResponseEntity.ok(hashMapOf("mode" to true, "member" to member, "list" to resultMap))
//        basketFlux.concatMap {
//            val distinctFruits = Flux.fromIterable(it).distinct().collectList()
//            val countFruitsMono = Flux.fromIterable(it)
//                .groupBy { fruit -> fruit }
//                .concatMap ({ groupedFlux -> groupedFlux.count()
//                    .map {
//                        val fruitCount = linkedMapOf<String, Long>()
//                        fruitCount.put(groupedFlux.key(), it)
//                        return fruitCount
//                    }
//                })
//
//        }
    }
}