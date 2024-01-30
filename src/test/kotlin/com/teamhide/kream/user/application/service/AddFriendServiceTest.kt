package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.exception.AlreadyFriendException
import com.teamhide.kream.user.application.exception.ExceedFriendLimitException
import com.teamhide.kream.user.application.port.out.GetUserFriendPersistencePort
import com.teamhide.kream.user.application.port.out.SaveUserFriendPersistencePort
import com.teamhide.kream.user.makeAddFriendCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AddFriendServiceTest : BehaviorSpec({
    val getUserFriendPersistencePort = mockk<GetUserFriendPersistencePort>()
    val saveUserFriendPersistencePort = mockk<SaveUserFriendPersistencePort>()
    val addFriendService = AddFriendService(
        getUserFriendPersistencePort = getUserFriendPersistencePort,
        saveUserFriendPersistencePort = saveUserFriendPersistencePort,
    )

    Given("이미 친구로 등록되어 있는 경우") {
        val command = makeAddFriendCommand(userId = 1L, friendUserId = 2L)
        every { getUserFriendPersistencePort.existsByUserIdAndFriendUserId(any(), any()) } returns true

        When("친구추가를 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<AlreadyFriendException> { addFriendService.execute(command) }
            }
        }
    }

    Given("등록 제한 개수만큼의 친구가 이미 등록되어 있는 경우") {
        val command = makeAddFriendCommand(userId = 1L, friendUserId = 2L)
        every { getUserFriendPersistencePort.existsByUserIdAndFriendUserId(any(), any()) } returns false
        every { getUserFriendPersistencePort.countsByUserIdLessThan(any(), any()) } returns false

        When("친구추가를 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<ExceedFriendLimitException> { addFriendService.execute(command) }
            }
        }
    }

    Given("등록 제한 개수보다 적은 친구를 가지고 있는 경우") {
        val command = makeAddFriendCommand(userId = 1L, friendUserId = 2L)
        every { getUserFriendPersistencePort.existsByUserIdAndFriendUserId(any(), any()) } returns false
        every { getUserFriendPersistencePort.countsByUserIdLessThan(any(), any()) } returns true
        every { saveUserFriendPersistencePort.save(any(), any()) } returns Unit

        When("친구추가를 요청하면") {
            addFriendService.execute(command)

            Then("정상적으로 처리된다") {
                verify(exactly = 1) { saveUserFriendPersistencePort.save(command.userId, command.friendUserId) }
            }
        }
    }
})
