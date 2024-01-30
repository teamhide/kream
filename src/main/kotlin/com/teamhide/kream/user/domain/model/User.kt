package com.teamhide.kream.user.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.user.domain.vo.Address
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table
class User(
    @Column(name = "email", nullable = false, length = 255)
    val email: String,

    @Column(name = "nickname", nullable = false, length = 20)
    val nickname: String,

    @Column(name = "password", nullable = false, length = 255)
    val password: String,

    @Embedded
    val address: Address,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    companion object {
        fun create(email: String, nickname: String, password1: String, password2: String, address: Address): User {
            if (password1 != password2) {
                throw PasswordDoesNotMatchException()
            }
            return User(email = email, nickname = nickname, password = password1, address = address)
        }
    }
}
