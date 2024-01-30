package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.port.`in`.GetFriendLocationsQuery
import com.teamhide.kream.user.application.port.out.GetUserFriendPersistencePort
import com.teamhide.kream.user.application.port.out.GetUserPersistencePort
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetFriendLocationsServiceTest : BehaviorSpec({
    val getUserPersistencePort = mockk<GetUserPersistencePort>()
    val getUserFriendPersistencePort = mockk<GetUserFriendPersistencePort>()
    val getFriendLocationsService = GetFriendLocationsService(
        getUserPersistencePort = getUserPersistencePort,
        getUserFriendPersistencePort = getUserFriendPersistencePort,
    )

    Given("userId = 1의 친구가 2, 3이 있을 때") {
        val targetUserId = 1L
        val user1 = makeUser(id = 2L)
        val user2 = makeUser(id = 3L)
        every {
            getUserFriendPersistencePort
                .findAllFriendUserIdsByUserId(targetUserId)
        } returns arrayListOf(2L, 3L)
        every {
            getUserPersistencePort
                .findAllByIdIn(arrayListOf(2L, 3L))
        } returns arrayListOf(user1, user2)
        val query = GetFriendLocationsQuery(userId = targetUserId)

        When("친구의 위치를 조회하면") {
            val sut = getFriendLocationsService.execute(query = query)

            Then("2, 3번 유저의 위치가 리턴된다") {
                sut.size shouldBe 2
                val friend1 = sut[0]
                friend1.userId shouldBe user1.id
                friend1.location shouldBe user1.location
                friend1.nickname shouldBe user1.nickname
                friend1.stayedAt shouldBe user1.stayedAt

                val friend2 = sut[1]
                friend2.userId shouldBe user2.id
                friend2.location shouldBe user2.location
                friend2.nickname shouldBe user2.nickname
                friend2.stayedAt shouldBe user2.stayedAt
            }
        }
    }
})
