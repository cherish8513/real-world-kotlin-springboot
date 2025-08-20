package com.joo.real_world.user.domain

import com.joo.real_world.user.domain.vo.UserId

class Follower(
    val followerId: UserId,
    val followeeId: UserId
)