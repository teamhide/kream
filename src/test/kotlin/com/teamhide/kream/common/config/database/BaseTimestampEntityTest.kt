package com.teamhide.kream.common.config.database

import com.teamhide.kream.user.makeUserEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class BaseTimestampEntityTest : StringSpec({
    "createdAt getter" {
        // Given
        val user = makeUserEntity()

        // When
        val createdAt = user.createdAt

        // Then
        createdAt.year shouldBe LocalDateTime.now().year
    }

    "updatedAt getter" {
        // Given
        val user = makeUserEntity()

        // When
        val updatedAt = user.updatedAt

        // Then
        updatedAt.year shouldBe LocalDateTime.now().year
    }

    "createdAt setter" {
        // Given
        val user = makeUserEntity()

        // When
        user.createdAt = LocalDateTime.now()

        // Then
        user.createdAt.year shouldBe LocalDateTime.now().year
    }

    "updatedAt setter" {
        // Given
        val user = makeUserEntity()

        // When
        user.updatedAt = LocalDateTime.now()

        // Then
        user.updatedAt.year shouldBe LocalDateTime.now().year
    }
})
