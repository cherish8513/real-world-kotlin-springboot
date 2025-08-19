package com.joo.real_world.security.config

import com.joo.real_world.common.config.ApiController
import org.springframework.web.bind.annotation.GetMapping

@ApiController
class DummyController {
    @GetMapping("/users")
    fun users() = "ok"
    @GetMapping("/users/login")
    fun login() = "ok"
    @GetMapping("/secret")
    fun secret() = "ok"
}