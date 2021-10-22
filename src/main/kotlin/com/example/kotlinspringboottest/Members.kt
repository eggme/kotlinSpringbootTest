package com.example.kotlinspringboottest

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Members (@Id @GeneratedValue val mbno: Int?){
    var name: String? = null
    var age: Int? = 0
    var done: Boolean = false
    var modifiedAt : LocalDateTime = LocalDateTime.now()
}