package com.teamhide.kream.product.domain.model

import com.teamhide.kream.common.config.database.BaseTimestampEntity
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "product_display")
class ProductDisplay(
    @Field(name = "product_id")
    val productId: Long,

    @Field(name = "last_bidding_id")
    var lastBiddingId: Long?,

    @Field(name = "name")
    val name: String,

    @Field(name = "price")
    var price: Int,

    @Field(name = "brand")
    val brand: String,

    @Field(name = "category")
    val category: String,

    @Id
    val id: ObjectId = ObjectId(),
) : BaseTimestampEntity() {
    fun changePrice(price: Int) {
        this.price = price
    }

    fun changeLastBiddingId(biddingId: Long) {
        this.lastBiddingId = biddingId
    }
}
