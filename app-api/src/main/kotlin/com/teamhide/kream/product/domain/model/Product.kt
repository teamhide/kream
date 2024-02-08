package com.teamhide.kream.product.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import com.teamhide.kream.product.domain.vo.SizeType
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
import jakarta.persistence.Table

@Entity
@Table(name = "product")
class Product(
    @Column(name = "name", nullable = false, length = 80)
    val name: String,

    @Column(name = "release_price", nullable = false)
    val releasePrice: Int,

    @Column(name = "model_number", nullable = false, length = 50)
    val modelNumber: String,

    @Column(name = "size_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val sizeType: SizeType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_brand_id")
    val productBrand: ProductBrand,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    val productCategory: ProductCategory,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseTimestampEntity() {
    init {
        if (releasePrice <= 0) {
            throw InvalidReleasePriceException()
        }
    }
}
