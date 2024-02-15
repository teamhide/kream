package com.teamhide.kream.common.outbox

import com.teamhide.kream.support.test.JpaRepositoryTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@JpaRepositoryTest
internal class OutboxQuerydslRepositoryImplTest(
    private val outboxRepository: OutboxRepository,
    private val entityManager: EntityManager,
) {
    @Test
    fun `완료되지 않은 로우를 조회한다`() {
        // Given
        val outbox1 = Outbox(
            aggregateType = AggregateType.BIDDING_COMPLETED,
            payload = "payload",
            completedAt = null,
        )
        val outbox2 = Outbox(
            aggregateType = AggregateType.BIDDING_COMPLETED,
            payload = "payload",
            completedAt = null,
        )
        outboxRepository.saveAll(listOf(outbox1, outbox2))

        // When
        val pageRequest = PageRequest.of(0, 1)
        val sut = outboxRepository.findAllBy(pageable = pageRequest)

        // Then
        sut.size shouldBe 1
        val outbox = sut[0]
        outbox.id shouldBe outbox1.id
        outbox.completedAt shouldBe null
        outbox.aggregateType shouldBe outbox1.aggregateType
        outbox.payload shouldBe outbox1.payload
    }

    @Test
    @Transactional
    fun `id목록으로 완료처리한다`() {
        // Given
        val outbox1 = Outbox(
            aggregateType = AggregateType.BIDDING_COMPLETED,
            payload = "payload",
            completedAt = null,
        )
        val outbox2 = Outbox(
            aggregateType = AggregateType.BIDDING_COMPLETED,
            payload = "payload",
            completedAt = null,
        )
        outboxRepository.saveAll(listOf(outbox1, outbox2))

        // When
        val count = outboxRepository.completeByIds(outboxIds = listOf(outbox1.id, outbox2.id))
        entityManager.flush()
        entityManager.clear()

        // Then
        count shouldBe 2
        val outboxes = outboxRepository.findAll()
        val sut1 = outboxes[0]
        sut1.id shouldBe outbox1.id
        sut1.completedAt shouldNotBe null
        sut1.aggregateType shouldBe outbox1.aggregateType
        sut1.payload shouldBe outbox1.payload

        val sut2 = outboxes[1]
        sut2.id shouldBe outbox2.id
        sut2.completedAt shouldNotBe null
        sut2.aggregateType shouldBe outbox2.aggregateType
        sut2.payload shouldBe outbox2.payload
    }
}
