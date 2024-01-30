package com.teamhide.kream.product.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product_brand")
class ProductBrand(
    @Column(name = "name", nullable = false, length = 80)
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity()
