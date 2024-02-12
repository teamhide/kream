package com.teamhide.kream.product

import com.teamhide.kream.product.adapter.`in`.api.v1.BidRequest
import com.teamhide.kream.product.adapter.`in`.api.v1.RegisterProductRequest
import com.teamhide.kream.product.adapter.out.persistence.jpa.ProductInfoDto
import com.teamhide.kream.product.domain.event.BiddingCreatedEvent
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.model.ProductBrand
import com.teamhide.kream.product.domain.model.ProductCategory
import com.teamhide.kream.product.domain.model.ProductDisplay
import com.teamhide.kream.product.domain.model.ProductInfo
import com.teamhide.kream.product.domain.usecase.RegisterProductCommand
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayCommand
import com.teamhide.kream.product.domain.vo.BiddingType
import com.teamhide.kream.product.domain.vo.SizeType

fun makeProductCategory(
    id: Long = 1L,
    name: String = "categoryName",
    parentCategoryId: Long = 1L,
): ProductCategory {
    return ProductCategory(
        name = name,
        parentCategoryId = parentCategoryId,
        id = id,
    )
}

fun makeProductBrand(
    id: Long = 1L,
    name: String = "brandName",
): ProductBrand {
    return ProductBrand(id = id, name = name)
}

fun makeProduct(
    id: Long = 1L,
    name: String = "productName",
    releasePrice: Int = 1000,
    modelNumber: String = "A-123",
    sizeType: SizeType = SizeType.CLOTHES,
    productBrand: ProductBrand = makeProductBrand(),
    productCategory: ProductCategory = makeProductCategory(),
): Product {
    return Product(
        id = id,
        name = name,
        releasePrice = releasePrice,
        modelNumber = modelNumber,
        sizeType = sizeType,
        productBrand = productBrand,
        productCategory = productCategory,
    )
}

fun makeRegisterProductCommand(
    name: String = "name",
    releasePrice: Int = 1000,
    modelNumber: String = "A-123",
    sizeType: SizeType = SizeType.CLOTHES,
    brandId: Long = 1L,
    categoryId: Long = 1L,
): RegisterProductCommand {
    return RegisterProductCommand(
        name = name,
        releasePrice = releasePrice,
        modelNumber = modelNumber,
        sizeType = sizeType,
        brandId = brandId,
        categoryId = categoryId,
    )
}

fun makeRegisterProductRequest(
    name: String = "CLUNY",
    releasePrice: Int = 59000,
    modelNumber: String = "B-123",
    sizeType: SizeType = SizeType.CLOTHES,
    brandId: Long = 1L,
    categoryId: Long = 1L,
): RegisterProductRequest {
    return RegisterProductRequest(
        name = name,
        releasePrice = releasePrice,
        modelNumber = modelNumber,
        sizeType = sizeType,
        brandId = brandId,
        categoryId = categoryId,
    )
}

fun makeBidRequest(
    productId: Long = 1L,
    price: Int = 50000,
    size: String = "M",
    biddingType: BiddingType = BiddingType.SALE,
): BidRequest {
    return BidRequest(
        productId = productId,
        price = price,
        size = size,
        biddingType = biddingType,
    )
}

fun makeProductDisplay(
    productId: Long = 1L,
    name: String = "name",
    price: Int = 10000,
    brand: String = "Nike",
    category: String = "SHOES",
    lastBiddingId: Long = 1L,
): ProductDisplay {
    return ProductDisplay(
        productId = productId,
        name = name,
        price = price,
        brand = brand,
        category = category,
        lastBiddingId = lastBiddingId,
    )
}

fun makeSaveOrUpdateProductDisplayCommand(
    productId: Long = 1L,
    price: Int = 20000,
    biddingId: Long = 1L,
): SaveOrUpdateProductDisplayCommand {
    return SaveOrUpdateProductDisplayCommand(
        productId = productId,
        price = price,
        biddingId = biddingId,
    )
}

fun makeBiddingCreatedEvent(
    productId: Long = 1L,
    biddingType: String = "PURCHASE",
    price: Int = 10000,
    biddingId: Long = 1L,
): BiddingCreatedEvent {
    return BiddingCreatedEvent(
        productId = productId,
        biddingType = biddingType,
        price = price,
        biddingId = biddingId,
    )
}

fun makeProductInfo(
    productId: Long = 1L,
    releasePrice: Int = 20000,
    modelNumber: String = "A-123",
    name: String = "name",
    brand: String = "NIKE",
    category: String = "SHOES",
): ProductInfo {
    return ProductInfo(
        productId = productId,
        releasePrice = releasePrice,
        modelNumber = modelNumber,
        name = name,
        brand = brand,
        category = category,
    )
}

fun makeProductInfoDto(
    productId: Long = 1L,
    releasePrice: Int = 20000,
    modelNumber: String = "A-123",
    name: String = "SAKAI",
    brand: String = "NIKE",
    category: String = "SHOES",
): ProductInfoDto {
    return ProductInfoDto(
        productId = productId,
        releasePrice = releasePrice,
        modelNumber = modelNumber,
        name = name,
        brand = brand,
        category = category,
    )
}
