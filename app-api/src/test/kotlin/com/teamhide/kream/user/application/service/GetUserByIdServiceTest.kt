package com.teamhide.kream.user.application.service

import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.domain.usecase.GetUserByIdQuery
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
internal class GetUserByIdServiceTest(
    private val userRepository: UserRepository,
    private val getUserByIdService: GetUserByIdService,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("GetUserByIdService") {
        val query = GetUserByIdQuery(userId = 1L)
        val user = userRepository.save(makeUser())

        When("유저 ID를 기반으로 조회하면") {
            val sut = getUserByIdService.execute(query = query)

            Then("유저 정보가 반환된다") {
                sut.shouldNotBeNull()
                sut.id shouldBe user.id
                sut.password shouldBe user.password
                sut.email shouldBe user.email
                sut.nickname shouldBe user.nickname
                sut.address shouldBe user.address
            }
        }
    }
})
