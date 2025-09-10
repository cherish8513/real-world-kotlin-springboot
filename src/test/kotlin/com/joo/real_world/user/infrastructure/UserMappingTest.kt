package com.joo.real_world.user.infrastructure

import com.joo.real_world.user.domain.User
import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserMappingTest {

    @Test
    fun `User to UserEntity mapping`() {
        // given
        val user = User(
            id = UserId(1L),
            username = "testuser",
            email = Email.of("test@example.com"),
            password = Password.of("password123"),
            bio = "bio",
            image = "image.png"
        )

        // when
        val entity = user.toEntity()

        // then
        assertThat(entity.id).isEqualTo(user.id!!.value)
        assertThat(entity.username).isEqualTo(user.username)
        assertThat(entity.email).isEqualTo(user.email.value)
        assertThat(entity.password).isEqualTo(user.password.value)
        assertThat(entity.bio).isEqualTo(user.bio)
        assertThat(entity.image).isEqualTo(user.image)
    }

    @Test
    fun `UserEntity to User mapping`() {
        // given
        val entity = UserEntity(
            id = 2L,
            username = "entityuser",
            email = "entity@example.com",
            password = "password456",
            bio = "entity bio",
            image = "entity.png"
        )

        // when
        val user = entity.toDomain()

        // then
        assertThat(user.id!!.value).isEqualTo(entity.id)
        assertThat(user.username).isEqualTo(entity.username)
        assertThat(user.email.value).isEqualTo(entity.email)
        assertThat(user.password.value).isEqualTo(entity.password)
        assertThat(user.bio).isEqualTo(entity.bio)
        assertThat(user.image).isEqualTo(entity.image)
    }
}
