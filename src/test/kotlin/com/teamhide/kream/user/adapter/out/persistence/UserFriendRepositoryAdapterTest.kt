package com.teamhide.kream.user.adapter.out.persistence

import com.teamhide.kream.user.adapter.out.persistence.jpa.UserFriendRepository
import com.teamhide.kream.user.makeUserFriendEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserFriendRepositoryAdapterTest : StringSpec({
    val userFriendRepository = mockk<UserFriendRepository>()
    val repositoryAdapter = UserFriendRepositoryAdapter(userFriendRepository = userFriendRepository)

    "userId와 friendUserId로 이미 로우가 존재하는지 확인한다" {
        // Given
        val userId = 1L
        val friendUserId = 2L
        every { userFriendRepository.existsByUserIdAndFriendUserId(any(), any()) } returns false

        // When
        val sut = repositoryAdapter.existsByUserIdAndFriendUserId(userId = userId, friendUserId = friendUserId)

        // Then
        sut shouldBe false
    }

    "userId와 friendUserId로 로우를 저장한다" {
        // Given
        val userId = 1L
        val friendUserId = 2L
        val userFriendEntity = makeUserFriendEntity()
        every { userFriendRepository.save(any()) } returns userFriendEntity

        // When
        repositoryAdapter.save(userId = userId, friendUserId = friendUserId)

        // Then
        verify(exactly = 1) { userFriendRepository.save(any()) }
    }

    "특정 유저의 로우 개수가 count보다 작은지 확인한다" {
        // Given
        val userFriend1 = makeUserFriendEntity(userId = 1L, friendUserId = 2L)
        val userFriend2 = makeUserFriendEntity(userId = 1L, friendUserId = 3L)
        val userFriend3 = makeUserFriendEntity(userId = 1L, friendUserId = 4L)
        val friends = arrayListOf(userFriend1, userFriend2, userFriend3)
        every { userFriendRepository.findAllByUserId(userId = 1L, pageable = any()) } returns friends

        // When
        val sut = repositoryAdapter.countsByUserIdLessThan(userId = 1L, count = 2)

        // Then
        sut shouldBe false
    }

    "userId에 해당하는 유저의 모든 친구들의 id 목록을 조회한다" {
        // Given
        val userId = 1L
        val userFriendEntity = makeUserFriendEntity()
        every { userFriendRepository.findAllByUserId(any()) } returns arrayListOf(userFriendEntity)

        // When
        val sut = repositoryAdapter.findAllFriendUserIdsByUserId(userId = userId)

        // Then
        sut.size shouldBe 1
        sut[0] shouldBe userFriendEntity.friendUserId
    }

    "userId의 친구 목록에 friendUserId가 있는지 확인한다" {
        // Given
        val userId = 1L
        val friendUserId = 2L
        every { userFriendRepository.findByUserIdAndFriendUserId(userId, friendUserId) } returns null

        // When
        val sut = repositoryAdapter.isFriendWith(userId = userId, friendUserId = friendUserId)

        // Then
        sut shouldBe false
    }
})
