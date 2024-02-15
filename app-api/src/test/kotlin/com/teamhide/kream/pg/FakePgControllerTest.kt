package com.teamhide.kream.pg

import com.teamhide.kream.client.makePgCancelPaymentRequest
import com.teamhide.kream.support.test.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.post

const val URL = "/pg"

internal class FakePgControllerTest : BaseIntegrationTest() {
    @Test
    fun `결제를 시도한다`() {
        // Given, When, Then
        mockMvc.post("$URL/payment")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `결제를 취소한다`() {
        // Given
        val request = makePgCancelPaymentRequest()

        // When, Then
        mockMvc.post("$URL/cancel") {
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
            }
    }
}
