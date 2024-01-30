package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "user_friend",
    indexes = [
        Index(name = "idx__user_id_friend_user_id", columnList = "user_id, friend_user_id")
    ]
)
class UserFriendEntity(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "friend_user_id", nullable = false)
    val friendUserId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity()
