package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.vo.Email
import com.joo.real_world.user.domain.vo.Password
import com.joo.real_world.user.domain.vo.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserTest {

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        // Given: a user exists
        user = User(
            id = UserId(1L),
            email = Email.of("original@example.com"),
            username = "originalUser",
            password = Password.of("newPass"),
            bio = "Original bio",
            image = "original.png"
        )
    }

    @Test
    fun `can change email`() {
        // When: email is changed
        val updated = user.change(
            email = "new@example.com",
            username = null,
            password = null,
            bio = null,
            image = null
        )

        // Then: only email should be updated
        assertThat(updated.email.value).isEqualTo("new@example.com")
        assertThat(updated.username).isEqualTo(user.username)
        assertThat(updated.password).isEqualTo(user.password)
        assertThat(updated.bio).isEqualTo(user.bio)
        assertThat(updated.image).isEqualTo(user.image)
    }

    @Test
    fun `can change username`() {
        val updated = user.change(
            email = null,
            username = "newUser",
            password = null,
            bio = null,
            image = null
        )

        assertThat(updated.username).isEqualTo("newUser")
        assertThat(updated.email).isEqualTo(user.email)
    }

    @Test
    fun `can change password`() {
        val updated = user.change(
            email = null,
            username = null,
            password = Password.of("newPass"),
            bio = null,
            image = null
        )

        assertThat(updated.password.value).isEqualTo("newPass")
        assertThat(updated.username).isEqualTo(user.username)
    }

    @Test
    fun `can change bio`() {
        val updated = user.change(
            email = null,
            username = null,
            password = null,
            bio = "New bio",
            image = null
        )

        assertThat(updated.bio).isEqualTo("New bio")
    }

    @Test
    fun `can change image`() {
        val updated = user.change(
            email = null,
            username = null,
            password = null,
            bio = null,
            image = "new.png"
        )

        assertThat(updated.image).isEqualTo("new.png")
    }

    @Test
    fun `can change multiple fields at once`() {
        val updated = user.change(
            email = "new@example.com",
            username = "newUser",
            password = Password.of("newPass"),
            bio = "New bio",
            image = "new.png"
        )

        assertThat(updated.email.value).isEqualTo("new@example.com")
        assertThat(updated.username).isEqualTo("newUser")
        assertThat(updated.password.value).isEqualTo("newPass")
        assertThat(updated.bio).isEqualTo("New bio")
        assertThat(updated.image).isEqualTo("new.png")
    }

    @Test
    fun `changing with null values keeps existing fields`() {
        val updated = user.change(
            email = null,
            username = null,
            password = null,
            bio = null,
            image = null
        )

        assertThat(updated.email).isEqualTo(user.email)
        assertThat(updated.username).isEqualTo(user.username)
        assertThat(updated.password).isEqualTo(user.password)
        assertThat(updated.bio).isEqualTo(user.bio)
    }
}
