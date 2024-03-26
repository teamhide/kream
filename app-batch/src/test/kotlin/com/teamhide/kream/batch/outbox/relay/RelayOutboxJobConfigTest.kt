package com.teamhide.kream.batch.outbox.relay

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.batch.test.BatchTest
import com.teamhide.kream.batch.test.MongoDbCleaner
import com.teamhide.kream.batch.test.MysqlDbCleaner
import com.teamhide.kream.common.outbox.AggregateType
import com.teamhide.kream.common.outbox.Outbox
import com.teamhide.kream.common.outbox.OutboxRepository
import com.teamhide.kream.product.application.service.BiddingKafkaAdapter
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull

@MockkBean(value = [BiddingKafkaAdapter::class])
@BatchTest
class RelayOutboxJobConfigTest(
    @Qualifier(RelayOutboxJobConfig.JOB_NAME) private val job: Job,
    @Autowired private val jobLauncherTestUtils: JobLauncherTestUtils,
    private val objectMapper: ObjectMapper,
    private val outboxRepository: OutboxRepository,
    private val biddingKafkaAdapter: BiddingKafkaAdapter,
) : BehaviorSpec({
    listeners(MysqlDbCleaner(), MongoDbCleaner())

    Given("Outbox 테이블에 이벤트가 존재할 때") {
        jobLauncherTestUtils.job = job
        val event = BiddingCompletedEvent(productId = 1L, biddingId = 1L)
        val outbox = outboxRepository.save(
            Outbox(
                aggregateType = AggregateType.BIDDING_COMPLETED,
                payload = objectMapper.writeValueAsString(event),
            )
        )
        every { biddingKafkaAdapter.sendBiddingCompleted(any()) } returns Unit

        When("Relay 배치를 실행하면") {
            val jobExecution = jobLauncherTestUtils.launchJob()

            Then("배치 결과가 성공이다") {
                jobExecution.status shouldBe BatchStatus.COMPLETED
            }

            Then("Outbox 이벤트가 완료 처리되었다") {
                val completedOutbox = outboxRepository.findByIdOrNull(outbox.id)
                completedOutbox.shouldNotBeNull()
                completedOutbox.completedAt.shouldNotBeNull()
            }

            Then("카프카로 메시지가 전송된다") {
                verify(exactly = 1) { biddingKafkaAdapter.sendBiddingCompleted(any<BiddingCompletedEvent>()) }
            }
        }
    }
})
