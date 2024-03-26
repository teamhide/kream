package com.teamhide.kream.coupon.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponDto
import com.teamhide.kream.coupon.domain.usecase.RegisterCouponUseCase
import com.teamhide.kream.coupon.domain.vo.CouponDiscountType
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import com.teamhide.kream.support.test.RestControllerTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post

private const val URL = "/v1/coupon"

@WebMvcTest(CouponV1Controller::class)
internal class CouponV1ControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var registerCouponUseCase: RegisterCouponUseCase

    @Test
    fun `쿠폰 그룹을 생성한다`() {
        // Given
        val responseDto = RegisterCouponDto(
            identifier = "identifier",
            quantity = 100,
        )
        val request = RegisterCouponRequest(
            discountType = CouponDiscountType.AMOUNT,
            discountValue = 2000,
            quantity = 100,
            periodType = CouponPeriodType.DAY,
            period = 30,
        )
        val response = RegisterCouponResponse(
            identifier = responseDto.identifier,
            quantity = responseDto.quantity,
        )
        every { registerCouponUseCase.execute(any()) } returns responseDto

        // When, Then
        mockMvc.post(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }.andExpect {
            status { isOk() }
            jsonResponse(response)
        }
    }
}
