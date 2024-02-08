package com.teamhide.kream.common.util.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.nio.charset.MalformedInputException
import java.nio.charset.StandardCharsets
import java.util.Date

const val TWELVE_HOURS_TO_SECONDS = 86400
const val USER_ID_KEY = "user_id"

class TokenProvider(
    private val secretKey: String,
    private val expireSeconds: Int = TWELVE_HOURS_TO_SECONDS
) {

    fun encrypt(userId: Long): String {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
        return Jwts.builder()
            .setClaims(makeClaims(userId = userId))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun decrypt(token: String): TokenPayload {
        val userId = try {
            getClaims(token = token)[USER_ID_KEY]?.toString()?.toLong() ?: throw DecodeTokenException()
        } catch (e: Exception) {
            throw DecodeTokenException()
        }
        return TokenPayload(userId = userId)
    }

    private fun makeClaims(userId: Long): Claims {
        val expiredAt = Date(Date().time + expireSeconds)
        val claims = Jwts.claims().setIssuedAt(Date()).setExpiration(expiredAt)
        claims[USER_ID_KEY] = userId
        return claims
    }

    private fun getClaims(token: String): Claims {
        val parser = Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray(StandardCharsets.UTF_8))
            .build()

        try {
            return parser.parseClaimsJws(token).body
        } catch (e: Exception) {
            when (e) {
                is UnsupportedJwtException,
                is MalformedInputException,
                is SignatureException,
                is IllegalArgumentException -> throw DecodeTokenException()
                else -> throw e
            }
        }
    }
}
