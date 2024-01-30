
package com.teamhide.kream.user.adapter.out.persistence

import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserRepositoryAdapterTest : StringSpec({
    val userRepository = mockk<UserRepository>()
    val repositoryAdapter = UserRepositoryAdapter(userRepository = userRepository)

    beforeEach {
        clearAllMocks()
    }

    "email에 해당하는 유저가 있는지 확인한다" {
        // Given
        every { userRepository.existByEmail(email = "h@id.e") } returns true

        // When
        val sut = repositoryAdapter.existsByEmail(email = "h@id.e")

        // Then
        sut shouldBe true
        verify(exactly = 1) { userRepository.existByEmail(any()) }
    }

    "유저를 저장한다" {
        // Given
        val user = makeUser()
        every { userRepository.save(any()) } returns user

        // When
        val sut = repositoryAdapter.save(user = user)

        // Then
        sut.id shouldBe user.id
        sut.password shouldBe user.password
        sut.email shouldBe user.email
        sut.nickname shouldBe user.nickname
        sut.address shouldBe user.address
        verify(exactly = 1) { userRepository.save(any()) }
    }
})
