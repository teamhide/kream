package com.teamhide.kream.support.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.common.security.CustomAuthenticationEntryPoint
import com.teamhide.kream.common.security.JwtAuthenticationFilter
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@TestEnvironment
@WebMvcTest
abstract class RestControllerTest {
    @MockkBean
    lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    lateinit var mockMvc: MockMvc

    @BeforeEach
    internal fun setUp(
        webApplicationContext: WebApplicationContext,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .addFilter<DefaultMockMvcBuilder>(jwtAuthenticationFilter)
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    fun MockHttpServletRequestDsl.jsonRequest(value: Any) {
        content = objectMapper.writeValueAsString(value)
        contentType = MediaType.APPLICATION_JSON
    }

    fun MockMvcResultMatchersDsl.jsonResponse(value: Any) {
        content { objectMapper.writeValueAsString(value) }
    }
}
