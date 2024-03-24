package com.teamhide.kream.product.application.service

import com.teamhide.kream.user.domain.usecase.GetUserByIdUseCase
import com.teamhide.kream.user.makeUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class ProductUserAdapterTest : StringSpec({
    val getUserByIdUseCase = mockk<GetUserByIdUseCase>()
    val productUserAdapter = ProductUserAdapter(getUserByIdUseCase = getUserByIdUseCase)

    "ID로 유저를 조회한다" {
        // Given
        val userId = 1L
        val user = makeUser()
        every { getUserByIdUseCase.execute(any()) } returns user

        // When
        val sut = productUserAdapter.findById(userId = userId)

        // Then
        sut.shouldNotBeNull()
        sut.id shouldBe user.id
        sut.password shouldBe user.password
        sut.email shouldBe user.email
        sut.nickname shouldBe user.nickname
        sut.address shouldBe user.address
    }
})
