package com.teamhide.kream.common.aop

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.common.exception.GlobalExceptionHandler
import com.teamhide.kream.common.response.ApiResponse
import com.teamhide.kream.common.util.lock.LockAcquireFailException
import com.teamhide.kream.common.util.lock.LockKeys
import com.teamhide.kream.common.util.lock.RedisLock
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val URL = "/test/lock"

@RestController
@RequestMapping(URL)
class TestLockController(
    private val testLockService: TestLockService,
) {
    @GetMapping("")
    fun testLock(): ApiResponse<Void> {
        testLockService.execute()
        return ApiResponse.success(statusCode = HttpStatus.OK)
    }
}

@Component
class TestLockService {
    @RedisLock(key = LockKeys.IMMEDIATE_SALE)
    fun execute() {}
}

@SpringBootTest
class RedisLockAspectTest {
    @Autowired
    lateinit var testLockController: TestLockController

    @MockkBean
    lateinit var redissonClient: RedissonClient

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(testLockController)
            .setControllerAdvice(GlobalExceptionHandler())
            .build()
    }

    @Test
    fun `tryLock에서 InterruptedException이 발생하면 예외를 반환한다`() {
        // Given
        val mockRLock = mockk<RLock>()
        every { mockRLock.tryLock(any(), any(), any()) } throws InterruptedException()
        every { redissonClient.getLock(any<String>()) } returns mockRLock
        val exc = LockAcquireFailException()

        // When, Then
        mockMvc.get(URL) {
        }
            .andExpect {
                status { isRequestTimeout() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }

    @Test
    fun `락 획득에 실패하면 예외를 반환한다`() {
        // Given
        val mockRLock = mockk<RLock>()
        every { mockRLock.tryLock(any(), any(), any()) } returns false
        every { redissonClient.getLock(any<String>()) } returns mockRLock
        every { mockRLock.unlock() } returns Unit
        val exc = LockAcquireFailException()

        // When, Then
        mockMvc.get(URL) {
        }
            .andExpect {
                status { isRequestTimeout() }
                jsonPath("errorCode") { value(exc.errorCode) }
            }
    }
}
