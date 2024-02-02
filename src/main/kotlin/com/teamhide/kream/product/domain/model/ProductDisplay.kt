package com.teamhide.kream.product.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "product_display")
class ProductDisplay(
    @Field(name = "product_id")
    val productId: Long,

    @Field(name = "name")
    val name: String,

    @Field(name = "price")
    val price: Int,

    @Field(name = "brand")
    val brand: String,

    @Field(name = "category")
    val category: String,
) : BaseTimestampEntity()
