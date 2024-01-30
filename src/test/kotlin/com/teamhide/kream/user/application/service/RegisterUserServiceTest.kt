package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.exception.UserAlreadyExistException
import com.teamhide.kream.user.application.port.out.GetUserPersistencePort
import com.teamhide.kream.user.application.port.out.SaveUserPersistencePort
import com.teamhide.kream.user.makeRegisterUserCommand
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class RegisterUserServiceTest : BehaviorSpec({
    val getUserPersistencePort = mockk<GetUserPersistencePort>()
    val saveUserPersistencePort = mockk<SaveUserPersistencePort>()
    val registerUserService = RegisterUserService(
        getUserPersistencePort = getUserPersistencePort, saveUserPersistencePort = saveUserPersistencePort,
    )

    Given("동일한 이메일 또는 닉네임을 가진 유저가 존재하는 경우") {
        val command = makeRegisterUserCommand()
        val user = makeUser()
        every { getUserPersistencePort.findByEmailOrNickname(any(), any()) } returns user

        When("회원가입을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserAlreadyExistException> { registerUserService.execute(command) }
            }
        }
    }

    Given("동일한 이메일 또는 닉네임을 가진 유저가 존재하지 않는 경우") {
        val command = makeRegisterUserCommand()
        val user = makeUser()
        every { getUserPersistencePort.findByEmailOrNickname(any(), any()) } returns null
        every { saveUserPersistencePort.save(any()) } returns user

        When("회원가입을 요청하면") {
            val sut = registerUserService.execute(command)

            Then("유저가 저장된다") {
                sut.id shouldBe user.id
                sut.nickname shouldBe user.nickname
                sut.email shouldBe user.email
                sut.location.lat shouldBe user.location.lat
                sut.location.lng shouldBe user.location.lng
                sut.stayedAt shouldBe user.stayedAt
                sut.status shouldBe user.status
            }
        }
    }
})
