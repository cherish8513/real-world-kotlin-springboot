package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface IUserJpaRepository : JpaRepository<UserEntity, Long>

@Repository
class UserJpaRepository(
    private val userJpaRepository: IUserJpaRepository
) : UserRepository {
    override fun save(user: User): User {
        val userEntity = userJpaRepository.save(UserMapper.toEntity(user))

        return UserMapper.toDomain(userEntity)
    }
}