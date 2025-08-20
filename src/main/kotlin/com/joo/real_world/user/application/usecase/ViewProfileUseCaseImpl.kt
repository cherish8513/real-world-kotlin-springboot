package com.joo.real_world.user.application.usecase

import com.joo.real_world.common.exception.CustomExceptionType
import com.joo.real_world.common.util.assertNotNull
import com.joo.real_world.user.application.ProfileDto
import com.joo.real_world.user.domain.FollowRepository
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Exception::class])
@Service
class ViewProfileUseCaseImpl(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) : ViewProfileUseCase {
    override fun getProfile(username: String, viewerId: Long?): ProfileDto {
        val user = userRepository.findByUsername(username).assertNotNull(CustomExceptionType.INVALID_USER)

        return ProfileDto(
            username = user.username,
            bio = user.bio,
            image = user.image,
            following = viewerId?.let {
                followRepository.isFollowing(
                    followerId = UserId(it),
                    followeeId = user.id.assertNotNull()
                )
            } ?: false
        )
    }
}