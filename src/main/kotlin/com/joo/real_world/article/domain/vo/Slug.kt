package com.joo.real_world.article.domain.vo

@JvmInline
value class Slug(val value: String) {
    override fun toString(): String = value

    companion object {
        fun fromTitle(title: Title): Slug {
            return Slug(
                title
                    .value
                    .lowercase()
                    .replace("[^a-z0-9\\s]".toRegex(), "")
                    .trim()
                    .replace("\\s+".toRegex(), "-")
            )
        }
    }
}