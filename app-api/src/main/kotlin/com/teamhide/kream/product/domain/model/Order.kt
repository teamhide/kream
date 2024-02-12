package com.teamhide.kream.product.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.product.domain.vo.OrderStatus
import com.teamhide.kream.user.domain.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @Column(name = "payment_id", nullable = false, length = 255)
    val paymentId: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidding_id")
    val bidding: Bidding,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    val status: OrderStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity()
