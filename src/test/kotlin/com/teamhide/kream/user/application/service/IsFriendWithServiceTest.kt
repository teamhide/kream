package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.port.`in`.IsFriendWithQuery
import com.teamhide.kream.user.application.port.out.GetUserFriendPersistencePort
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class IsFriendWithServiceTest : BehaviorSpec({
    val getUserFriendPersistencePort = mockk<GetUserFriendPersistencePort>()
    val isFriendWithService = IsFriendWithService(getUserFriendPersistencePort = getUserFriendPersistencePort)

    Given("1번 유저가 2번 유저를") {
        every { getUserFriendPersistencePort.isFriendWith(userId = 1L, friendUserId = 2L) } returns false
        val query = IsFriendWithQuery(userId = 1L, friendUserId = 2L)

        When("친구로 등록했는지 확인을 요청하면") {
            val sut = isFriendWithService.execute(query = query)

            Then("결과를 반환한다") {
                sut shouldBe false
            }
        }
    }
})
