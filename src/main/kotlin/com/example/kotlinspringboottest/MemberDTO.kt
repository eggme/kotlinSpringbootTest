package com.example.kotlinspringboottest

import java.time.LocalDateTime

class MemberDTO {
    var mbno: Int? = 0
    var name: String? = ""
    var age: Int? = 0
    var done: Boolean = false
    var createAt: LocalDateTime? = null
    var modifiedAt: LocalDateTime? = null
}