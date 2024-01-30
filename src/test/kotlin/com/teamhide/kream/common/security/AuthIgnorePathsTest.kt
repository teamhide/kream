package com.teamhide.kream.common.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpMethod

class AuthIgnorePathsTest : StringSpec({
    "제외 대상 path이지만 method가 다른 경우 false가 반환된다" {
        val sut = AuthIgnorePaths.contain(AuthType.JWT, HttpMethod.POST, "/actuator/health")
        sut shouldBe false
    }

    "제외 대상이 아닌 엔드포인트는 false가 반환된다" {
        val sut = AuthIgnorePaths.contain(AuthType.JWT, HttpMethod.DELETE, "/non-asd")
        sut shouldBe false
    }

    "특정 인증 타입에 아무런 엔드포인트가 저장되어있지 않으면 false가 반환된다" {
        val sut = AuthIgnorePaths.contain(AuthType.INTERNAL, HttpMethod.DELETE, "/non-asd")
        sut shouldBe false
    }

    "제외 대상 엔드포인트는 true가 반환된다" {
        val sut = AuthIgnorePaths.contain(AuthType.JWT, HttpMethod.GET, "/actuator/health")
        sut shouldBe true
    }
})
