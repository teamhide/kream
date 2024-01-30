package com.teamhide.kream.user.adapter.out.persistence.jpa

import com.teamhide.kream.test.RepositoryTest
import com.teamhide.kream.user.domain.vo.UserStatus
import com.teamhide.kream.user.makeUserEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

@RepositoryTest
class UserQuerydslRepositoryImplTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `유저의 lat, lng를 업데이트한다`() {
        // Given
        val user = makeUserEntity()
        val savedUser = userRepository.save(user)
        val lat = 40.5555
        val lng = 120.1111

        // When
        val count = userRepository.updateLocationById(userId = savedUser.id, lat = lat, lng = lng)

        // Then
        count shouldBe 1
        val sut = userRepository.findByIdOrNull(savedUser.id)!!
        sut.id shouldBe savedUser.id
        sut.password shouldBe savedUser.password
        sut.email shouldBe savedUser.email
        sut.nickname shouldBe savedUser.nickname
        sut.status shouldBe savedUser.status
        sut.location.x shouldBe lat
        sut.location.y shouldBe lng
        sut.stayedAt shouldBe savedUser.stayedAt
    }

    @Test
    fun `유저의 status를 업데이트한다`() {
        // Given
        val user = makeUserEntity(status = UserStatus.ONLINE)
        val savedUser = userRepository.save(user)

        // When
        val count = userRepository.updateStatusById(userId = savedUser.id, status = UserStatus.GHOST)

        // Then
        count shouldBe 1
        val sut = userRepository.findByIdOrNull(savedUser.id)!!
        sut.id shouldBe savedUser.id
        sut.password shouldBe savedUser.password
        sut.email shouldBe savedUser.email
        sut.nickname shouldBe savedUser.nickname
        sut.status shouldBe UserStatus.GHOST
        sut.location.x shouldBe savedUser.location.x
        sut.location.y shouldBe savedUser.location.y
        sut.stayedAt shouldBe savedUser.stayedAt
    }
}
