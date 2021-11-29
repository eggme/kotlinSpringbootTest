package com.example.kotlinspringboottest

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Members (@Id @GeneratedValue val mbno: Int?){
    var name: String? = null
    var age: Int? = 0
    var done: Boolean = false
    // 테스트
    @CreatedDate val createdAt: LocalDateTime = LocalDateTime.now()
    @LastModifiedDate var modifiedAt : LocalDateTime = LocalDateTime.now()
}