package com.teamhide.kream.coupon.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.coupon.domain.usecase.CouponGroupDto
import com.teamhide.kream.coupon.domain.usecase.GetAllCouponUseCase
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
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

private const val URL = "/v1/coupon"

@WebMvcTest(CouponV1Controller::class)
internal class CouponV1ControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var registerCouponUseCase: RegisterCouponUseCase

    @MockkBean
    private lateinit var getAllCouponUseCase: GetAllCouponUseCase

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

    @Test
    fun `쿠폰 그룹 목록을 조회한다`() {
        // Given
        val couponGroup1 = CouponGroupDto(
            identifier = "iden1",
            discountType = CouponDiscountType.AMOUNT,
            discountValue = 1000,
            quantity = 20,
            remainQuantity = 20,
            periodType = CouponPeriodType.DAY,
            period = 30,
        )
        val couponGroup2 = CouponGroupDto(
            identifier = "iden2",
            discountType = CouponDiscountType.AMOUNT,
            discountValue = 2000,
            quantity = 30,
            remainQuantity = 20,
            periodType = CouponPeriodType.DAY,
            period = 20,
        )
        every { getAllCouponUseCase.execute(any()) } returns listOf(couponGroup1, couponGroup2)

        val response = GetAllCouponResponse(data = listOf(couponGroup1, couponGroup2))

        // When, Then
        mockMvc.get(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            param("pageSize", "10")
            param("offset", "0")
        }.andExpect {
            status { isOk() }
            jsonResponse(response)
        }
    }
}
