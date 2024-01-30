package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.port.`in`.UpdateUserLocationCommand
import com.teamhide.kream.user.application.port.out.UpdateUserPersistencePort
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UpdateUserLocationServiceTest : BehaviorSpec({
    val updateUserPersistencePort = mockk<UpdateUserPersistencePort>()
    val updateUserLocationService = UpdateUserLocationService(updateUserPersistencePort = updateUserPersistencePort)

    Given("유저의 위치가 주어졌을 때") {
        val userId = 1L
        val lat = 10.1234
        val lng = 100.1234
        val command = UpdateUserLocationCommand(userId = userId, lat = lat, lng = lng)
        every { updateUserPersistencePort.updateLocationById(any(), any()) } returns 1L

        When("id를 기반으로 유저 위치를 업데이트 요청하면") {
            val count = updateUserLocationService.execute(command = command)

            Then("정상 업데이트된다") {
                count shouldBe 1
                verify(exactly = 1) { updateUserPersistencePort.updateLocationById(any(), any()) }
            }
        }
    }
})
