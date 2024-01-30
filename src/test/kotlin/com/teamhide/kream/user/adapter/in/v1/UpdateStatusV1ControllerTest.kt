package com.teamhide.kream.user.adapter.`in`.v1

import com.teamhide.kream.test.BaseIntegrationTest
import com.teamhide.kream.user.USER_ID_1_TOKEN
import com.teamhide.kream.user.adapter.out.persistence.jpa.UserRepository
import com.teamhide.kream.user.domain.vo.UserStatus
import com.teamhide.kream.user.makeUserEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.put

class UpdateStatusV1ControllerTest : BaseIntegrationTest() {
    private val URL = "/api/v1/user/status"

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `유저의 상태를 업데이트한다`() {
        // Given
        val request = UpdateStatusRequest(status = UserStatus.GHOST)
        val userEntity = makeUserEntity(id = 1L)
        val savedUser = userRepository.save(userEntity)

        // When, Then
        mockMvc.put(URL) {
            header(HttpHeaders.AUTHORIZATION, "Bearer $USER_ID_1_TOKEN")
            jsonRequest(request)
        }
            .andExpect {
                status { isOk() }
            }

        val sut = userRepository.findByIdOrNull(id = savedUser.id)!!
        sut.status shouldBe request.status
    }
}
