package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.repository.BiddingRepository
import com.teamhide.kream.product.domain.repository.OrderRepository
import com.teamhide.kream.product.domain.repository.SaleHistoryRepository
import com.teamhide.kream.product.domain.vo.BiddingStatus
import com.teamhide.kream.product.domain.vo.OrderStatus
import com.teamhide.kream.product.makeBidding
import com.teamhide.kream.product.makeCompleteBidCommand
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import com.teamhide.kream.user.application.exception.UserNotFoundException
import com.teamhide.kream.user.domain.repository.UserRepository
import com.teamhide.kream.user.makeUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull

@IntegrationTest
internal class CompleteBidServiceTest(
    private val completeBidService: CompleteBidService,
    private val biddingRepository: BiddingRepository,
    private val userRepository: UserRepository,
    private val saleHistoryRepository: SaleHistoryRepository,
    private val orderRepository: OrderRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner())

    Given("존재하지 않는 유저가") {
        val command = makeCompleteBidCommand()

        When("입찰 종료 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotFoundException> { completeBidService.execute(command = command) }
            }
        }
    }

    Given("존재하지 않는 입찰에 대해") {
        val command = makeCompleteBidCommand()
        val user = makeUser()
        userRepository.save(user)

        When("입찰 종료 요청을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BiddingNotFoundException> { completeBidService.execute(command = command) }
            }
        }
    }

    Given("입찰에 대해") {
        val bidding = biddingRepository.save(makeBidding(status = BiddingStatus.IN_PROGRESS))

        val command = makeCompleteBidCommand(biddingId = bidding.id)

        val user = userRepository.save(makeUser())

        When("입찰 종료 요청을 하면") {
            completeBidService.execute(command = command)

            Then("입찰이 거래 완료 처리된다") {
                val savedBidding = biddingRepository.findByIdOrNull(bidding.id)
                savedBidding.shouldNotBeNull()
                savedBidding.status shouldBe BiddingStatus.COMPLETE
            }

            Then("판매 내역이 저장된다") {
                val savedHistory = saleHistoryRepository.findByBiddingId(biddingId = bidding.id)
                savedHistory.shouldNotBeNull()
                savedHistory.bidding.id shouldBe bidding.id
                savedHistory.user.id shouldBe user.id
            }

            Then("주문이 생성된다") {
                val savedOrder = orderRepository.findByBiddingId(biddingId = bidding.id)
                savedOrder.shouldNotBeNull()
                savedOrder.bidding.id shouldBe bidding.id
                savedOrder.user.id shouldBe user.id
                savedOrder.status shouldBe OrderStatus.COMPLETE
                savedOrder.paymentId shouldBe command.paymentId
            }
        }
    }
})
