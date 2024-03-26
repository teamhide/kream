package com.teamhide.kream.coupon.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.coupon.domain.usecase.AcquireCouponUseCase
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

private const val URL = "/v1/coupon/acquire"

@WebMvcTest(AcquireCouponV1Controller::class)
internal class AcquireCouponV1ControllerTest : RestControllerTest() {

    @MockkBean
    private lateinit var acquireCouponUseCase: AcquireCouponUseCase

    @Test
    fun `쿠폰 획득 API`() {
        // Given
        every { acquireCouponUseCase.execute(any()) } returns Unit

        // When, Then
        mockMvc.post("$URL/couponIdentifier") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
        }.andExpect {
            status { isOk() }
        }
    }
}
