
package com.teamhide.kream.user.domain.repository

import com.teamhide.kream.support.test.JpaRepositoryTest
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@JpaRepositoryTest
internal class UserQuerydslRepositoryImplTest(
    private val userRepository: UserRepository,
) : StringSpec({
    "email에 해당하는 유저가 있는지 확인한다" {
        // Given
        val email = "h@id.e"
        val user = makeUser(email = email)
        userRepository.save(user)

        // When
        val sut = userRepository.existByEmail(email = email)

        // Then
        sut shouldBe true
    }
})
