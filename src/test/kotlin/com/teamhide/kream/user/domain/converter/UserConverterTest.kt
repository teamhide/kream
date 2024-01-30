package com.teamhide.kream.user.domain.converter

import com.teamhide.kream.user.makeUser
import com.teamhide.kream.user.makeUserEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

internal class UserConverterTest : StringSpec({
    "entity -> domain 변환" {
        // Given
        val user = makeUserEntity()

        // When
        val sut = UserConverter.from(user = user)

        // Then
        sut.id shouldBe user.id
        sut.password shouldBe user.password
        sut.email shouldBe user.email
        sut.nickname shouldBe user.nickname
        sut.status shouldBe user.status
        sut.location.lat shouldBe user.location.x
        sut.location.lng shouldBe user.location.y
        sut.stayedAt shouldBe user.stayedAt
    }

    "domain -> entity 변환" {
        // Given
        val user = makeUser()

        // When
        val sut = UserConverter.to(user = user)

        // Then
        sut.id shouldBe user.id
        sut.password shouldBe user.password
        sut.email shouldBe user.email
        sut.nickname shouldBe user.nickname
        sut.status shouldBe user.status
        sut.location.x shouldBe user.location.lat
        sut.location.y shouldBe user.location.lng
        sut.stayedAt shouldBe user.stayedAt
    }
})
