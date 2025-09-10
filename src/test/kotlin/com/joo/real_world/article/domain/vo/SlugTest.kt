package com.joo.real_world.article.domain.vo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SlugTest {

    @Test
    fun `fromTitle converts normal title to slug`() {
        // Given
        val title = Title("My Test Article")

        // When
        val slug = Slug.fromTitle(title)

        // Then
        assertThat(slug.value).isEqualTo("my-test-article")
    }

    @Test
    fun `fromTitle trims spaces and replaces multiple spaces with single dash`() {
        val title = Title("   My    Test   Article   ")

        val slug = Slug.fromTitle(title)

        assertThat(slug.value).isEqualTo("my-test-article")
    }

    @Test
    fun `fromTitle removes special characters`() {
        val title = Title("Hello, World! @2025 #Test")

        val slug = Slug.fromTitle(title)

        assertThat(slug.value).isEqualTo("hello-world-2025-test")
    }

    @Test
    fun `fromTitle handles title with only special characters`() {
        val title = Title("!!! ### $$$")

        val slug = Slug.fromTitle(title)

        assertThat(slug.value).isEqualTo("")
    }

    @Test
    fun `toString returns slug value`() {
        val title = Title("Sample Title")
        val slug = Slug.fromTitle(title)

        assertThat(slug.toString()).isEqualTo(slug.value)
    }

    @Test
    fun `fromTitle handles uppercase letters correctly`() {
        val title = Title("This IS a TeSt")

        val slug = Slug.fromTitle(title)

        assertThat(slug.value).isEqualTo("this-is-a-test")
    }

    @Test
    fun `fromTitle handles mixed letters, numbers and spaces`() {
        val title = Title("Kotlin 5 Mini Test 2025")

        val slug = Slug.fromTitle(title)

        assertThat(slug.value).isEqualTo("kotlin-5-mini-test-2025")
    }
}
