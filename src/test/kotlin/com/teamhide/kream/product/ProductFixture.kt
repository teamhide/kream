package com.teamhide.kream.product

import com.teamhide.kream.bidding.adapter.`in`.api.v1.BidRequest
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.adapter.`in`.api.v1.RegisterProductRequest
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.model.ProductBrand
import com.teamhide.kream.product.domain.model.ProductCategory
import com.teamhide.kream.product.domain.usecase.RegisterProductCommand
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