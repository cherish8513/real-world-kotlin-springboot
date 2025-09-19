package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.follow.application.FollowQueryService
import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class UserQueryUseCaseImpl(
    private val userRepository: UserRepository,
    private val followQueryService: FollowQueryService
) : UserQueryUseCase {
    override fun getProfile(username: String, viewerId: Long?): ProfileDto {
        val user = userRepository.findByUsername(username).assertNotNull(CustomExceptionType.INVALID_USER)

        return ProfileDto(
            username = user.username,
            bio = user.bio,
            image = user.image,
            following = viewerId?.let {
                followQueryService.isFollowing(
                    followerId = it,
                    followeeId = user.id.assertNotNull().value
                )
            } ?: false
        )
    }
}