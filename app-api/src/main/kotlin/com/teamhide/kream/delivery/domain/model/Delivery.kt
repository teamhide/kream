package com.teamhide.kream.delivery.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.delivery.domain.vo.DeliveryStatus
import com.teamhide.kream.product.domain.model.Bidding
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(
    name = "delivery",
    indexes = [Index(name = "idx__status", columnList = "status")]
)
class Delivery(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidding_id")
    val bidding: Bidding,

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val status: DeliveryStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    companion object {
        fun start(bidding: Bidding): Delivery {
            return Delivery(bidding = bidding, status = DeliveryStatus.PENDING)
        }
    }
}
