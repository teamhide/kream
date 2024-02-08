package com.teamhide.kream.common.util.jwt

import com.teamhide.kream.user.EXPIRED_TOKEN
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.nio.charset.MalformedInputException

const val secretKey = "hidehidehidehidehidehidehidehidehide"

class TokenProviderTest : StringSpec({
    val provider = TokenProvider(secretKey = secretKey)

    beforeEach {
        clearAllMocks()
    }

    "토큰을 발행한다" {
        // Given, When
        val sut = provider.encrypt(userId = 1)

        // Then
        sut shouldStartWith "ey"
    }

    "토큰을 복호화한다" {
        // Given, When
        val sut = provider.decrypt(token = USER_ID_1_TOKEN)

        // Then
        sut.userId shouldBe 1
    }

    "유효하지 않은 토큰인 경우 예외가 발생한다" {
        // Given, When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = "abc") }
    }

    "만료된 토큰이면 예외가 발생한다" {
        // Given, When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = EXPIRED_TOKEN) }
    }

    "UnsupportedJwtException 토큰이면 예외가 발생한다" {
        // Given
        mockkStatic(Jwts::class)
        every {
            Jwts.parserBuilder().setSigningKey(any<ByteArray>()).build().parseClaimsJws(any()).body
        } throws UnsupportedJwtException("unsupported jwt")

        // When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = "abc") }
    }

    "MalformedInputException 토큰이면 예외가 발생한다" {
        // Given
        mockkStatic(Jwts::class)
        every {
            Jwts.parserBuilder().setSigningKey(any<ByteArray>()).build().parseClaimsJws(any()).body
        } throws MalformedInputException(10)

        // When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = "abc") }
    }

    "SignatureException 토큰이면 예외가 발생한다" {
        // Given
        mockkStatic(Jwts::class)
        every {
            Jwts.parserBuilder().setSigningKey(any<ByteArray>()).build().parseClaimsJws(any()).body
        } throws SignatureException("exc")

        // When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = "abc") }
    }

    "IllegalArgumentException 토큰이면 예외가 발생한다" {
        // Given
        mockkStatic(Jwts::class)
        every {
            Jwts.parserBuilder().setSigningKey(any<ByteArray>()).build().parseClaimsJws(any()).body
        } throws IllegalArgumentException("exc")

        // When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = "abc") }
    }

    "복호화 시 유저 ID키를 조회했을 때 빈 값이면 예외가 발생한다" {
        // Given
        mockkStatic(Jwts::class)
        val claims = mockk<Claims>()
        every { claims[USER_ID_KEY] } returns null
        every {
            Jwts.parserBuilder().setSigningKey(any<ByteArray>()).build().parseClaimsJws(any()).body
        } returns claims

        // When, Then
        shouldThrow<DecodeTokenException> { provider.decrypt(token = "abc") }
    }
})
