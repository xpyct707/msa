package com.xpyct707.msa.easywallet.rest

import com.xpyct707.msa.platformcommon.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController (private val userService: UserService) {
    @GetMapping
    fun hello(): String {
        return userService.helloMsg()
    }
}