package com.teamhide.kream.bidding.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.user.domain.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "sale_history")
class SaleHistory(
    @ManyToOne
    @JoinColumn(name = "bidding_id")
    val bidding: Bidding,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "price", nullable = false)
    val price: Int,

    @Column(name = "size", nullable = false, length = 5)
    val size: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity()
