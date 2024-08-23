package com.teamhide.kream.product.application.service

import com.ninjasquad.springmockk.MockkBean
import com.teamhide.kream.client.pg.AttemptPaymentResponse
import com.teamhide.kream.client.pg.PgClient
import com.teamhide.kream.common.outbox.AggregateType
import com.teamhide.kream.common.outbox.AggregateTypeMapper
import com.teamhide.kream.common.outbox.OutboxRepository
import com.teamhide.kream.product.application.exception.AlreadyCompleteBidException
import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.repository.OrderRepository
import com.teamhide.kream.product.domain.repository.ProductRepository
import com.teamhide.kream.product.domain.repository.SaleHistoryRepository
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.domain.vo.OrderStatus
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeImmediatePurchaseCommand
import com.teamhide.kream.product.makeProduct
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.data.repository.findByIdOrNull

@MockkBean(value = [PgClient::class])
@IntegrationTest
internal class ImmediatePurchaseServiceTest(
    private val immediatePurchaseService: ImmediatePurchaseService,
    private val biddingRepository: BiddingRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val saleHistoryRepository: SaleHistoryRepository,
    private val orderRepository: OrderRepository,
    private val outboxRepository: OutboxRepository,
    private val pgClient: PgClient,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())
    isolationMode = IsolationMode.InstancePerLeaf

    Given("ImmediatePurchaseService") {
        When("즉시 구매 요청을 하면") {
            val command = makeImmediatePurchaseCommand()

            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { immediatePurchaseService.execute(command = command) }
            }
        }

        When("진행중이 아닌 판매 입찰에 대해 즉시 구매를 요청하면") {
            val command = makeImmediatePurchaseCommand()

            val bidding = makeBidding(biddingType = BiddingType.SALE, status = BiddingStatus.COMPLETE)
            biddingRepository.save(bidding)

            Then("예외가 발생한다") {
                shouldThrow<AlreadyCompleteBidException> { immediatePurchaseService.execute(command = command) }
            }
        }

        When("존재하지 않는 유저가 즉시 구매를 요청하면") {
            val command = makeImmediatePurchaseCommand()

            val bidding = makeBidding(biddingType = BiddingType.SALE, status = BiddingStatus.IN_PROGRESS)
            biddingRepository.save(bidding)

            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { immediatePurchaseService.execute(command = command) }
            }
        }

        When("판매 입찰에 대해 즉시 구매를 요청하면") {
            val seller = userRepository.save(makeUser(id = 1L))
            val purchaser = userRepository.save(makeUser(id = 2L))
            val command = makeImmediatePurchaseCommand(userId = purchaser.id)

            val product = productRepository.save(makeProduct(id = 1L))

            val bidding = makeBidding(
                id = command.biddingId,
                product = product,
                user = seller,
                biddingType = BiddingType.SALE,
                status = BiddingStatus.IN_PROGRESS,
            )
            val savedBidding = biddingRepository.save(bidding)

            val paymentId = "paymentId"
            every { pgClient.attemptPayment(any()) } returns AttemptPaymentResponse(paymentId = paymentId)

            val sut = immediatePurchaseService.execute(command = command)

            Then("구매 정보가 리턴된다") {
                sut.biddingId shouldBe bidding.id
                sut.price shouldBe bidding.price
            }

            Then("Bidding이 완료 상태로 변경되었다") {
                val sutBidding = biddingRepository.findByIdOrNull(id = savedBidding.id)
                sutBidding.shouldNotBeNull()
                sutBidding.status shouldBe BiddingStatus.COMPLETE
            }

            Then("판매 내역이 생성되었다") {
                val saleHistory = saleHistoryRepository.findByBiddingId(biddingId = savedBidding.id)
                saleHistory.shouldNotBeNull()
                saleHistory.bidding.id shouldBe savedBidding.id
                saleHistory.user.id shouldBe bidding.user.id
                saleHistory.price shouldBe savedBidding.price
                saleHistory.size shouldBe savedBidding.size
            }

            Then("주문이 생성되었다") {
                val order = orderRepository.findByBiddingId(biddingId = savedBidding.id)
                order.shouldNotBeNull()
                order.paymentId shouldBe paymentId
                order.bidding.id shouldBe savedBidding.id
                order.user.id shouldBe purchaser.id
                order.status shouldBe OrderStatus.COMPLETE
            }

            Then("아웃박스 테이블에 이벤트가 저장되었다") {
                val outboxes = outboxRepository.findAll()
                outboxes.size shouldBe 1

                val outbox = outboxes[0]
                outbox.aggregateType shouldBe AggregateType.BIDDING_COMPLETED
                outbox.completedAt shouldBe null
                val payload = AggregateTypeMapper.from<BiddingCompletedEvent>(outbox.payload)
                payload.biddingId shouldBe savedBidding.id
                payload.productId shouldBe product.id
            }
        }
    }
})
