package com.teamhide.kream.common.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.product.domain.event.BiddingCreatedEvent
import com.teamhide.kream.product.domain.vo.BiddingType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AggregateTypeMapperTest : StringSpec({
    val objectMapper = ObjectMapper()

    "payload를 BiddingCreatedEvent로 변환한다" {
        // Given
        val event = BiddingCreatedEvent(
            productId = 1L,
            biddingType = BiddingType.SALE.name,
            price = 2000,
            biddingId = 1L,
        )
        val payload = objectMapper.writeValueAsString(event)

        // When
        val sut = AggregateTypeMapper.from<BiddingCreatedEvent>(payload = payload)

        // Then
        sut.productId shouldBe event.productId
        sut.biddingType shouldBe event.biddingType
        sut.price shouldBe event.price
        sut.biddingId shouldBe event.biddingId
    }
})
