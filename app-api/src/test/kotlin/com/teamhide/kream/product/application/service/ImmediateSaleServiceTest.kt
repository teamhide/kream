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
import com.teamhide.kream.product.makeImmediateSaleCommand
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
internal class ImmediateSaleServiceTest(
    private val immediateSaleService: ImmediateSaleService,
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

    Given("존재하지 않는 구매 입찰에 대해") {
        val command = makeImmediateSaleCommand()

        When("즉시 판매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { immediateSaleService.execute(command = command) }
            }
        }
    }

    Given("진행중이 아닌 구매 입찰에 대해") {
        val command = makeImmediateSaleCommand()

        val bidding = makeBidding(biddingType = BiddingType.PURCHASE, status = BiddingStatus.COMPLETE)
        biddingRepository.save(bidding)

        When("즉시 판매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<AlreadyCompleteBidException> { immediateSaleService.execute(command = command) }
            }
        }
    }

    Given("존재하지 유저가") {
        val command = makeImmediateSaleCommand()

        val bidding = makeBidding(biddingType = BiddingType.PURCHASE, status = BiddingStatus.IN_PROGRESS)
        biddingRepository.save(bidding)

        When("즉시 판매 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { immediateSaleService.execute(command = command) }
            }
        }
    }

    Given("구매 입찰에 대해") {
        val command = makeImmediateSaleCommand()

        val seller = userRepository.save(makeUser(id = 1L))
        // purchaser
        userRepository.save(makeUser(id = 2L))

        val product = productRepository.save(makeProduct(id = 1L))
        val bidding = makeBidding(
            id = command.biddingId,
            product = product,
            user = seller,
            biddingType = BiddingType.PURCHASE,
            status = BiddingStatus.IN_PROGRESS,
        )
        val savedBidding = biddingRepository.save(bidding)

        val paymentId = "paymentId"
        every { pgClient.attemptPayment(any()) } returns AttemptPaymentResponse(paymentId = paymentId)

        When("즉시 판매 요청을 하면") {
            val sut = immediateSaleService.execute(command = command)

            Then("판매 정보가 리턴된다") {
                sut.biddingId shouldBe bidding.id
                sut.price shouldBe bidding.price
            }

            Then("Bidding이 완료 상태로 변경되었다") {
                val sut = biddingRepository.findByIdOrNull(id = savedBidding.id)
                sut.shouldNotBeNull()
                sut.status shouldBe BiddingStatus.COMPLETE
            }

            Then("판매 내역이 생성되었다") {
                val saleHistory = saleHistoryRepository.findByBiddingId(biddingId = savedBidding.id)
                saleHistory.shouldNotBeNull()
                saleHistory.bidding.id shouldBe savedBidding.id
                saleHistory.user.id shouldBe seller.id
                saleHistory.price shouldBe savedBidding.price
                saleHistory.size shouldBe savedBidding.size
            }

            Then("주문이 생성되었다") {
                val order = orderRepository.findByBiddingId(biddingId = savedBidding.id)
                order.shouldNotBeNull()
                order.paymentId shouldBe paymentId
                order.bidding.id shouldBe savedBidding.id
                order.user.id shouldBe seller.id
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
