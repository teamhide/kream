package com.teamhide.kream.product.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.product.domain.model.QProduct
import com.teamhide.kream.product.domain.model.QProductBrand
import com.teamhide.kream.product.domain.model.QProductCategory

class ProductQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : ProductQuerydslRepository {
    private val product = QProduct.product
    private val productCategory = QProductCategory.productCategory
    private val productBrand = QProductBrand.productBrand

    override fun findInfoById(productId: Long): ProductInfoDto? {
        return queryFactory
            .select(
                QProductInfoDto(
                    product.id,
                    product.releasePrice,
                    product.modelNumber,
                    product.name,
                    productBrand.name,
                    productCategory.name,
                )
            )
            .from(product)
            .innerJoin(product.productBrand, productBrand)
            .innerJoin(product.productCategory, productCategory)
            .where(product.id.eq(productId))
            .fetchFirst()
    }
}
