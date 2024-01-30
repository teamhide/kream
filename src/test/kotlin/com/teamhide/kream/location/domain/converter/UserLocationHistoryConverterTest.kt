package com.teamhide.kream.location.domain.converter

import com.teamhide.kream.location.makeUserLocationHistory
import com.teamhide.kream.location.makeUserLocationHistoryEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UserLocationHistoryConverterTest : StringSpec({
    "entity -> domain 변환" {
        // Given
        val locationHistory = makeUserLocationHistoryEntity()

        // When
        val sut = UserLocationHistoryConverter.from(locationHistory)

        // Then
        sut.id shouldBe locationHistory.id
        sut.userId shouldBe locationHistory.userId
        sut.location shouldBe locationHistory.location
    }

    "domain -> entity 변환" {
        // Given
        val locationHistory = makeUserLocationHistory()

        // When
        val sut = UserLocationHistoryConverter.to(locationHistory)

        // Then
        sut.id shouldBe locationHistory.id
        sut.userId shouldBe locationHistory.userId
        sut.location shouldBe locationHistory.location
    }
})
