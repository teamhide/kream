package com.teamhide.kream.user.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    @Column(name = "base_address", nullable = false, length = 20)
    var base: String,

    @Column(name = "detail_address", nullable = false, length = 20)
    var detail: String,
)
