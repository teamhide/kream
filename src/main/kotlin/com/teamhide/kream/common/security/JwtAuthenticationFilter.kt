package com.teamhide.kream.common.security

import com.teamhide.kream.common.util.jwt.DecodeTokenException
import com.teamhide.kream.common.util.jwt.TokenPayload
import com.teamhide.kream.common.util.jwt.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint
) : OncePerRequestFilter() {

    private var tokenProvider: TokenProvider = TokenProvider(secretKey = secretKey)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)
        authorization ?: run {
            authError(request = request, response = response)
            return
        }

        val token = extractToken(authorization = authorization)
        token ?: run {
            authError(request = request, response = response)
            return
        }

        val payload: TokenPayload
        try {
            payload = tokenProvider.decrypt(token)
        } catch (e: DecodeTokenException) {
            authError(request = request, response = response)
            return
        }

        val currentUser = CurrentUser(id = payload.userId)
        val authentication = UsernamePasswordAuthenticationToken(currentUser, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    fun authError(request: HttpServletRequest, response: HttpServletResponse) {
        SecurityContextHolder.clearContext()
        authenticationEntryPoint.commence(request = request, response = response, authException = JwtAuthenticationFailException())
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        val method = HttpMethod.valueOf(request.method)
        return AuthIgnorePaths.contain(authType = AuthType.JWT, method = method, path = path)
    }

    fun extractToken(authorization: String): String? {
        if (!authorization.startsWith("Bearer ")) {
            return null
        }
        return authorization.split(" ")[1]
    }
}
