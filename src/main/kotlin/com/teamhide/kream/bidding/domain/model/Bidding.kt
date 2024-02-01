package com.teamhide.kream.bidding.domain.model

import com.teamhide.kream.bidding.domain.vo.BiddingStatus
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.user.domain.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "bidding")
class Bidding(
    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "price", nullable = false)
    val price: Int,

    @Column(name = "size", nullable = false, length = 5)
    val size: String,

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var status: BiddingStatus,

    @Column(name = "bidding_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    val biddingType: BiddingType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    init {
        if (price <= 0) {
            throw InvalidBiddingPriceException()
        }
    }

    fun changeStatus(status: BiddingStatus) {
        this.status = status
    }
}
