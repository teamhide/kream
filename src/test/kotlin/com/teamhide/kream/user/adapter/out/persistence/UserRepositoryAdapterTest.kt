package com.teamhide.kream.user.adapter.out.persistence

import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.domain.vo.Location
import com.teamhide.kream.user.domain.vo.UserStatus
import com.teamhide.kream.user.makeUser
import com.teamhide.kream.user.makeUserEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class UserRepositoryAdapterTest : StringSpec({
    val userRepository = mockk<UserRepository>()
    val repositoryAdapter = UserRepositoryAdapter(userRepository = userRepository)

    beforeEach {
        clearAllMocks()
    }

    "email과 nickname으로 유저를 조회한다" {
        // Given
        val userEntity = makeUserEntity()
        every { userRepository.findByEmailOrNickname(any(), any()) } returns userEntity

        // When
        val sut = repositoryAdapter.findByEmailOrNickname(email = userEntity.email, nickname = userEntity.nickname)!!

        // Then
        sut.id shouldBe userEntity.id
        sut.password shouldBe userEntity.password
        sut.email shouldBe userEntity.email
        sut.nickname shouldBe userEntity.nickname
        sut.status shouldBe userEntity.status
        sut.location.lat shouldBe userEntity.location.x
        sut.location.lng shouldBe userEntity.location.y
        sut.stayedAt shouldBe userEntity.stayedAt
        verify(exactly = 1) { userRepository.findByEmailOrNickname(any(), any()) }
    }

    "유저를 저장한다" {
        // Given
        val user = makeUser()
        val userEntity = makeUserEntity()
        every { userRepository.save(any()) } returns userEntity

        // When
        val sut = repositoryAdapter.save(user = user)

        // Then
        sut.id shouldBe userEntity.id
        sut.password shouldBe userEntity.password
        sut.email shouldBe userEntity.email
        sut.nickname shouldBe userEntity.nickname
        sut.status shouldBe userEntity.status
        sut.location.lat shouldBe userEntity.location.x
        sut.location.lng shouldBe userEntity.location.y
        sut.stayedAt shouldBe userEntity.stayedAt
        verify(exactly = 1) { userRepository.save(any()) }
    }

    "유저 ID목록에 해당하는 로우를 리턴한다" {
        // Given
        val userIds = arrayListOf(1L, 2L)
        val userEntity = makeUserEntity()
        every { userRepository.findAllByIdIn(userIds) } returns arrayListOf(userEntity)

        // When
        val sut = repositoryAdapter.findAllByIdIn(userIds = userIds)

        // Then
        sut.size shouldBe 1
        val user = sut[0]
        user.id shouldBe userEntity.id
        user.password shouldBe userEntity.password
        user.email shouldBe userEntity.email
        user.nickname shouldBe userEntity.nickname
        user.status shouldBe userEntity.status
        user.location.lat shouldBe userEntity.location.x
        user.location.lng shouldBe userEntity.location.y
        user.stayedAt shouldBe userEntity.stayedAt
    }

    "id를 기반으로 유저의 위치를 업데이트한다" {
        // Given
        val userId = 1L
        val lat = 37.1234
        val lng = 120.1234
        every { userRepository.updateLocationById(any(), any(), any()) } returns 1L

        // When
        val count = repositoryAdapter.updateLocationById(userId = userId, location = Location(lat = lat, lng = lng))

        // Then
        count shouldBe 1
        verify(exactly = 1) { userRepository.updateLocationById(userId = userId, lat = lat, lng = lng) }
    }

    "id로 유저를 조회한다" {
        // Given
        val userEntity = makeUserEntity()
        every { userRepository.findByIdOrNull(any()) } returns userEntity

        // When
        val sut = repositoryAdapter.findById(id = userEntity.id)!!

        // Then
        sut.id shouldBe userEntity.id
        sut.password shouldBe userEntity.password
        sut.email shouldBe userEntity.email
        sut.nickname shouldBe userEntity.nickname
        sut.status shouldBe userEntity.status
        sut.location.lat shouldBe userEntity.location.x
        sut.location.lng shouldBe userEntity.location.y
        sut.stayedAt shouldBe userEntity.stayedAt
        verify(exactly = 1) { userRepository.findByIdOrNull(any()) }
    }

    "id로 status를 업데이트한다" {
        // Given
        val userId = 1L
        every { userRepository.updateStatusById(any(), any()) } returns 1L

        // When
        val count = repositoryAdapter.updateStatusById(userId = userId, status = UserStatus.GHOST)

        // Then
        count shouldBe 1
        verify(exactly = 1) { userRepository.updateStatusById(userId = userId, status = UserStatus.GHOST) }
    }
})
