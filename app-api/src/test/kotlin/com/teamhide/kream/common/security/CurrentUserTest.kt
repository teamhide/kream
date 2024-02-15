package com.teamhide.kream.common.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

internal class CurrentUserTest : StringSpec({
    "권한을 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.authorities

        sut.size shouldBe 0
    }

    "패스워드를 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.password

        sut shouldBe ""
    }

    "유저 이름을 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.username

        sut shouldBe ""
    }

    "계정이 만료되었는지 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.isAccountNonExpired

        sut shouldBe false
    }

    "계정이 잠겼는지 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.isAccountNonLocked

        sut shouldBe false
    }

    "credential이 만료되었는지 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.isCredentialsNonExpired

        sut shouldBe false
    }

    "isEnabled 상태를 확인한다" {
        val currentUser = CurrentUser(id = 1)

        val sut = currentUser.isEnabled

        sut shouldBe false
    }
})
