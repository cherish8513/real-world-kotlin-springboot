package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.UserRepository
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface IUserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun findByUsername(username: String): UserEntity?
}

@Repository
class UserJpaRepository(
    private val userJpaRepository: IUserJpaRepository
) : UserRepository {
    override fun save(user: User): User {
        val userEntity = userJpaRepository.save(user.toEntity())

        return userEntity.toDomain()
    }

    override fun findByEmail(email: Email): User? {
        return userJpaRepository.findByEmail(email.value)?.toDomain()
    }

    override fun findByUserId(userId: UserId): User? {
        return userJpaRepository.findByIdOrNull(userId.value)?.toDomain()
    }

    override fun findByUsername(username: String): User? {
        return userJpaRepository.findByUsername(username)?.toDomain()
    }
}