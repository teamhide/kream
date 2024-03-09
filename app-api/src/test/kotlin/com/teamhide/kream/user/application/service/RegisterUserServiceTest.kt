package com.teamhide.kream.user.application.service

import com.teamhide.kream.user.application.exception.UserAlreadyExistException
import com.teamhide.kream.user.domain.model.PasswordDoesNotMatchException
import com.teamhide.kream.user.domain.repository.UserRepositoryAdapter
import com.teamhide.kream.user.makeRegisterUserCommand
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class RegisterUserServiceTest : BehaviorSpec({
    val userRepositoryAdapter = mockk<UserRepositoryAdapter>()
    val registerUserService = RegisterUserService(userRepositoryAdapter = userRepositoryAdapter)

    Given("password1과 password2가 동일하지 않은 경우") {
        val command = makeRegisterUserCommand(password1 = "a", password2 = "b")

        When("회원가입을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<PasswordDoesNotMatchException> { registerUserService.execute(command) }
            }
        }
    }

    Given("동일한 이메일을 가진 유저가 존재하는 경우") {
        val command = makeRegisterUserCommand()
        every { userRepositoryAdapter.existsByEmail(any()) } returns true

        When("회원가입을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserAlreadyExistException> { registerUserService.execute(command) }
            }
        }
    }

    Given("동일한 이메일 또는 닉네임을 가진 유저가 존재하지 않는 경우") {
        val command = makeRegisterUserCommand()
        val user = makeUser()
        every { userRepositoryAdapter.existsByEmail(any()) } returns false
        every { userRepositoryAdapter.save(any()) } returns user

        When("회원가입을 요청하면") {
            val sut = registerUserService.execute(command)

            Then("유저가 저장된다") {
                sut.id shouldBe user.id
                sut.nickname shouldBe user.nickname
                sut.email shouldBe user.email
                sut.password shouldBe user.password
                sut.address shouldBe user.address
            }
        }
    }
})
