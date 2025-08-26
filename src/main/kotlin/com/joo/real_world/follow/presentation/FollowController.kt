package com.joo.real_world.follow.presentation

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.follow.application.usecase.FollowManagementUseCase
import com.joo.real_world.security.infrastructure.UserSession
import com.joo.real_world.user.presentation.response.ProfileResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@ApiController
@RequestMapping("/profiles")
class FollowController(
    private val followManagementUseCase: FollowManagementUseCase,
) {
    @PostMapping("/{username}/follow")
    fun followUser(@PathVariable username: String, @AuthenticationPrincipal userSession: UserSession): ProfileResponse {
        return followManagementUseCase.follow(followerId = userSession.userId, followeeUsername = username)
            .toProfileResponse()
    }

    @DeleteMapping("/{username}/follow")
    fun unfollowUser(
        @PathVariable username: String,
        @AuthenticationPrincipal userSession: UserSession
    ): ProfileResponse {
        return followManagementUseCase.unfollow(followerId = userSession.userId, followeeUsername = username)
            .toProfileResponse()
    }
}