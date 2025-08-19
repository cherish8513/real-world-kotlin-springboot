package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.value.Email
import com.joo.real_world.user.domain.value.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface IUserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}

@Repository
class UserJpaRepository(
    private val userJpaRepository: IUserJpaRepository
) : UserRepository {
    override fun save(user: User): User {
        val userEntity = userJpaRepository.save(UserMapper.toEntity(user))

        return UserMapper.toDomain(userEntity)
    }

    override fun findByEmail(email: Email): User? {
        return userJpaRepository.findByEmail(email.value)?.let { UserMapper.toDomain(it) }
    }

    override fun findByUserId(userId: UserId): User? {
        return userJpaRepository.findByIdOrNull(userId.value)?.let { UserMapper.toDomain(it) }
    }
}