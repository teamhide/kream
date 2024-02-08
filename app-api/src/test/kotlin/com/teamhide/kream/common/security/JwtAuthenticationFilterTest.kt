package com.teamhide.kream.common.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.common.response.ApiResponse
import io.kotest.core.spec.style.StringSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
internal class TestController {
    @GetMapping("/test")
    fun test(): ApiResponse<Void> {
        return ApiResponse.success(HttpStatus.OK)
    }
}

@SpringBootTest
internal class JwtAuthenticationFilterTest(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val objectMapper: ObjectMapper
) : StringSpec({
    lateinit var mockMvc: MockMvc

    beforeEach {
        val testController = TestController()
        mockMvc = MockMvcBuilders.standaloneSetup(testController).addFilter<StandaloneMockMvcBuilder>(jwtAuthenticationFilter)
            .build()
    }

    "토큰이 없는 경우 401을 리턴한다" {
        val exc = JwtAuthenticationFailException()
        mockMvc.get("/test")
            .andExpect {
                status { isUnauthorized() }
                content { objectMapper.writeValueAsString(exc) }
            }
    }

    "토큰이 Bearer로 시작하지 않는 경우 401을 리턴한다" {
        val exc = JwtAuthenticationFailException()
        mockMvc.get("/test") {
            header(name = HttpHeaders.AUTHORIZATION, "Bear ABC")
        }
            .andExpect {
                status { isUnauthorized() }
                content { objectMapper.writeValueAsString(exc) }
            }
    }

    "정상 토큰인 경우 200을 리턴한다" {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDE3NjQ3NzMsInVzZXJfaWQiOjF9.o2l9ZN4aCGhYMEslxwv6rtc-y7Oi8G_nz9OlfNk7kJk"

        mockMvc.get("/test") {
            header(name = HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andExpect {
                status { isOk() }
            }
    }

    "만료된 토큰인 경우 401을 리턴한다" {
        val exc = JwtAuthenticationFailException()
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDE3NTk5MTAsImV4cCI6MTcwMTc1OTk5NywidXNlcl9pZCI6MX0.a3gyosESbCJ_-adDmkPUUa7hrdx2zQe1xebUV252jb8"

        mockMvc.get("/test") {
            headers { }
            header(name = HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andExpect {
                status { isUnauthorized() }
                content { objectMapper.writeValueAsString(exc) }
            }
    }
})
