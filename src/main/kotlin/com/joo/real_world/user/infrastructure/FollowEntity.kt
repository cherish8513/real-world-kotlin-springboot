package com.joo.real_world.user.infrastructure

import com.joo.real_world.common.exception.CustomExceptionType
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable


@Entity
@Table(name = "follow")
class FollowEntity(
    @EmbeddedId
    val id: FollowerId
) {
    val followerId: Long
        get() = id.followerId ?: throw CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.toException("followerId is null")

    val followeeId: Long
        get() = id.followeeId ?: throw CustomExceptionType.UNEXPECTED_ERROR_OCCURRED.toException("followeeId is null")
}

@Embeddable
class FollowerId : Serializable {
    var followerId: Long? = null
    var followeeId: Long? = null

    constructor(followerId: Long, followeeId: Long) {
        this.followerId = followerId
        this.followeeId = followeeId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FollowerId) return false
        return followerId == other.followerId && followeeId == other.followeeId
    }

    override fun hashCode(): Int {
        var result = followerId?.hashCode() ?: 0
        result = 31 * result + (followeeId?.hashCode() ?: 0)
        return result
    }
}