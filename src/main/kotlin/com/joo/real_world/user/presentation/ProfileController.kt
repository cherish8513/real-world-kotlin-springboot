package com.joo.real_world.user.presentation

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.user.application.usecase.ViewProfileUseCase
import com.joo.real_world.user.presentation.response.ProfileResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping("/profiles")
class ProfileController(
    private val viewProfileUseCase: ViewProfileUseCase
) {
    @GetMapping("/{username}")
    fun getProfile(
        @PathVariable username: String,
        @AuthenticationPrincipal userSession: UserSession?
    ): ProfileResponse {
        return viewProfileUseCase.getProfile(username = username, viewerId = userSession?.userId).toProfileResponse()
    }

//    @PostMapping("/{username}/follow")
//    fun followUser(@PathVariable username: String, @AuthenticationPrincipal userSession: UserSession): ProfileResponse {
//        return
//    }
}