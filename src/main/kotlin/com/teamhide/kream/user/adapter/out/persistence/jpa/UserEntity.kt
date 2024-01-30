package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.user.domain.vo.UserStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime

@Entity
@Table(
    name = "user",
    indexes = [
        Index(name = "idx__nickname_email", columnList = "nickname, email"),
    ]
)
class UserEntity(
    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "nickname", nullable = false, length = 20)
    val nickname: String,

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val status: UserStatus,

    @Column(name = "location", columnDefinition = "POINT")
    val location: Point,

    @Column(name = "stayed_at", nullable = false)
    val stayedAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity()
