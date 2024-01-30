package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.port.`in`.UpdateLocationCommand
import com.teamhide.kream.location.application.port.out.UserExternalPort
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UpdateLocationServiceTest : BehaviorSpec({
    val userExternalPort = mockk<UserExternalPort>()
    val updateLocationService = UpdateLocationService(userExternalPort = userExternalPort)

    Given("유저 어그리거트에") {
        val command = UpdateLocationCommand(userId = 1L, lat = 10.1111, lng = 22.2222)
        every { userExternalPort.updateUserLocation(any(), any()) } returns 1L

        When("사용자의 위치를 업데이트 요청하면") {
            val count = updateLocationService.execute(command = command)

            Then("업데이트가 완료된다") {
                count shouldBe 1
                verify(exactly = 1) { userExternalPort.updateUserLocation(any(), any()) }
            }
        }
    }
})
