package com.teamhide.kream.location.application.service

import com.teamhide.kream.location.application.port.`in`.SaveUserLocationHistoryCommand
import com.teamhide.kream.location.application.port.out.SaveLocationHistoryPersistencePort
import com.teamhide.kream.location.domain.vo.UserLocation
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class SaveUserLocationHistoryServiceTest : BehaviorSpec({
    val persistencePort = mockk<SaveLocationHistoryPersistencePort>()
    val saveUserLocationHistoryService = SaveUserLocationHistoryService(
        persistencePort = persistencePort,
    )

    Given("유저 히스토리 커맨드를 통해") {
        val command = SaveUserLocationHistoryCommand(
            userId = 1L, location = UserLocation(lat = 37.123, lng = 127.123),
        )
        every { persistencePort.save(any()) } returns Unit

        When("저장을 시도하면") {
            saveUserLocationHistoryService.execute(command = command)

            Then("성공한다") {
                verify(exactly = 1) { persistencePort.save(any()) }
            }
        }
    }
})
