package com.teamhide.kream.user.application.service

import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import com.teamhide.kream.user.application.exception.UserAlreadyExistException
import com.teamhide.kream.user.domain.model.PasswordDoesNotMatchException
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeRegisterUserCommand
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class RegisterUserServiceTest(
    private val registerUserService: RegisterUserService,
    private val userRepository: UserRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

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
        userRepository.save(makeUser(email = command.email))

        When("회원가입을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserAlreadyExistException> { registerUserService.execute(command) }
            }
        }
    }

    Given("동일한 이메일 또는 닉네임을 가진 유저가 존재하지 않는 경우") {
        val command = makeRegisterUserCommand()
        userRepository.save(makeUser(email = "test@test.com"))

        When("회원가입을 요청하면") {
            val sut = registerUserService.execute(command)

            Then("유저가 저장된다") {
                sut.nickname shouldBe command.nickname
                sut.email shouldBe command.email
                sut.password shouldBe command.password1
                sut.address.base shouldBe command.baseAddress
                sut.address.detail shouldBe command.detailAddress
            }
        }
    }
})
