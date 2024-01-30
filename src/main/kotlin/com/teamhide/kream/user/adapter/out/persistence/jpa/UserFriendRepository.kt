package com.teamhide.kream.user.adapter.out.persistence.jpa

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UserFriendRepository : JpaRepository<UserFriendEntity, Long> {
    fun existsByUserIdAndFriendUserId(userId: Long, friendUserId: Long): Boolean

    fun findAllByUserId(userId: Long, pageable: Pageable): List<UserFriendEntity>

    fun findAllByUserId(userId: Long): List<UserFriendEntity>

    fun findByUserIdAndFriendUserId(userId: Long, friendUserId: Long): UserFriendEntity?
}
