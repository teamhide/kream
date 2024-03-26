package com.teamhide.kream.coupon.application.service

import com.teamhide.kream.coupon.application.exception.AlreadyAcquireException
import com.teamhide.kream.coupon.application.exception.CouponOutOfStockException
import com.teamhide.kream.coupon.application.exception.InvalidIdentifierException
import com.teamhide.kream.coupon.application.exception.UnavailableCouponGroupException
import com.teamhide.kream.coupon.domain.repository.CouponGroupRepository
import com.teamhide.kream.coupon.domain.repository.CouponHistoryRepository
import com.teamhide.kream.coupon.domain.repository.CouponRepository
import com.teamhide.kream.coupon.domain.vo.CouponGroupStatus
import com.teamhide.kream.coupon.domain.vo.CouponPeriodType
import com.teamhide.kream.coupon.domain.vo.CouponStatus
import com.teamhide.kream.coupon.makeAcquireCouponCommand
import com.teamhide.kream.coupon.makeCouponGroup
import com.teamhide.kream.support.test.IntegrationTest
import com.teamhide.kream.support.test.MysqlDbCleaner
import com.teamhide.kream.support.test.RedisCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@IntegrationTest
internal class AcquireCouponServiceTest(
    private val acquireCouponService: AcquireCouponService,
    private val redisTemplate: RedisTemplate<String, String>,
    private val couponGroupRepository: CouponGroupRepository,
    private val couponRedisAdapter: CouponRedisAdapter,
    private val couponRepository: CouponRepository,
    private val couponHistoryRepository: CouponHistoryRepository,
) : BehaviorSpec({
    listeners(MysqlDbCleaner(), RedisCleaner())

    Given("존재하지 않는 쿠폰 그룹 식별자로") {
        val command = makeAcquireCouponCommand()

        When("쿠폰 획득을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<InvalidIdentifierException> {
                    acquireCouponService.execute(command = command)
                }
            }
        }
    }

    Given("쿠폰 그룹의 상태가 ACTIVATED가 아닐 때") {
        val command = makeAcquireCouponCommand()

        val couponGroup = makeCouponGroup(status = CouponGroupStatus.DEACTIVATED)
        couponGroupRepository.save(couponGroup)

        When("쿠폰 획득을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<UnavailableCouponGroupException> {
                    acquireCouponService.execute(command = command)
                }
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수와 같은 경우") {
        val identifier = "uuid"
        val quantity = 100
        val currentQuantity = 100
        val command = makeAcquireCouponCommand(identifier = identifier, userId = 1L)

        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = quantity, identifier = identifier)
        couponGroupRepository.save(couponGroup)

        val key = couponRedisAdapter.makeKey(identifier = identifier)
        val userIds = Array(currentQuantity) { i -> (i + 1).toString() }
        redisTemplate.opsForSet().add(key, *userIds)

        When("쿠폰 획득을 요청하면") {
            Then("획득한 쿠폰은 회수 처리하고 예외가 발생한다") {
                shouldThrow<CouponOutOfStockException> {
                    acquireCouponService.execute(command = command)
                }
                val members = redisTemplate.opsForSet().members(key)
                members.shouldNotBeNull()
                members.size shouldBe currentQuantity
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수보다 큰 경우") {
        val identifier = "uuid"
        val quantity = 100
        val currentQuantity = 101
        val command = makeAcquireCouponCommand(identifier = identifier, userId = 200L)

        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = quantity, identifier = identifier)
        couponGroupRepository.save(couponGroup)

        val key = couponRedisAdapter.makeKey(identifier = identifier)
        val userIds = Array(currentQuantity) { i -> (i + 1).toString() }
        redisTemplate.opsForSet().add(key, *userIds)

        When("쿠폰 획득을 요청하면") {
            Then("쿠폰은 획득하지 못하고 예외가 발생한다") {
                shouldThrow<CouponOutOfStockException> {
                    acquireCouponService.execute(command = command)
                }
                val members = redisTemplate.opsForSet().members(key)
                members.shouldNotBeNull()
                members.size shouldBe currentQuantity
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수와 작지만 획득에 실패한 경우") {
        val identifier = "uuid"
        val quantity = 100
        val currentQuantity = 98
        val command = makeAcquireCouponCommand(identifier = identifier, userId = 1L)

        val couponGroup = makeCouponGroup(status = CouponGroupStatus.ACTIVATED, quantity = quantity, identifier = identifier)
        couponGroupRepository.save(couponGroup)

        val key = couponRedisAdapter.makeKey(identifier = identifier)
        val userIds = Array(currentQuantity) { i -> (i + 1).toString() }
        redisTemplate.opsForSet().add(key, *userIds)

        When("쿠폰 획득을 요청하면") {
            Then("획득한 쿠폰은 회수 처리하지 않고 예외가 발생한다") {
                shouldThrow<AlreadyAcquireException> {
                    acquireCouponService.execute(command = command)
                }
                val members = redisTemplate.opsForSet().members(key)
                members.shouldNotBeNull()
                members.size shouldBe currentQuantity
            }
        }
    }

    Given("발급된 쿠폰 개수가 총 개수보다 작을 때") {
        val identifier = "uuid"
        val quantity = 100
        val currentQuantity = 98
        val userId = 200L
        val command = makeAcquireCouponCommand(identifier = identifier, userId = userId)

        val periodType = CouponPeriodType.DAY
        val period = 1
        val couponGroup = makeCouponGroup(
            status = CouponGroupStatus.ACTIVATED,
            quantity = quantity,
            identifier = identifier,
            periodType = periodType,
            period = period,
        )
        val savedCouponGroup = couponGroupRepository.save(couponGroup)

        val key = couponRedisAdapter.makeKey(identifier = identifier)
        val userIds = Array(currentQuantity) { i -> (i + 1).toString() }
        redisTemplate.opsForSet().add(key, *userIds)

        When("쿠폰 획득을 요청하면") {
            acquireCouponService.execute(command = command)

            Then("레디스에 저장된 재고 개수가 증가한다") {
                val members = redisTemplate.opsForSet().members(key)
                members.shouldNotBeNull()
                members.size shouldBe currentQuantity + 1
            }

            Then("쿠폰이 생성된다") {
                val coupons = couponRepository.findAll()
                val sut = coupons[0]
                sut.userId shouldBe userId
                sut.couponGroup.id shouldBe savedCouponGroup.id
                sut.expiredAt.minusDays(period.toLong()).month shouldBe LocalDateTime.now().month
            }

            Then("쿠폰 그룹의 잔여 재고가 감소한다") {
                val sut = couponGroupRepository.findByIdOrNull(savedCouponGroup.id)
                sut.shouldNotBeNull()
                sut.remainQuantity shouldBe couponGroup.remainQuantity - 1
            }

            Then("쿠폰 발급 내역이 저장된다") {
                val histories = couponHistoryRepository.findAll()
                val sut = histories[0]
                sut.userId shouldBe userId
                sut.status shouldBe CouponStatus.ISSUED
            }
        }
    }
})
