package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.model.ProductDetail
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.repository.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.GetProductDetailQuery
import com.teamhide.kream.product.domain.usecase.GetProductDetailUseCase
import com.teamhide.kream.product.domain.vo.BiddingType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetProductDetailService(
    private val productRepositoryAdapter: ProductRepositoryAdapter,
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
) : GetProductDetailUseCase {
    override fun execute(query: GetProductDetailQuery): ProductDetail {
        val product = productRepositoryAdapter.findInfoById(productId = query.productId)
            ?: throw ProductNotFoundException()
        val expensiveBidding = biddingRepositoryAdapter.findMostExpensiveBidding(
            productId = product.productId,
            biddingType = BiddingType.PURCHASE
        )
        val cheapestBidding = biddingRepositoryAdapter.findMostCheapestBidding(
            productId = product.productId,
            biddingType = BiddingType.SALE
        )
        return ProductDetail(
            productId = product.productId,
            releasePrice = product.releasePrice,
            modelNumber = product.modelNumber,
            name = product.name,
            brand = product.brand,
            category = product.category,
            purchaseBidPrice = cheapestBidding?.price,
            saleBidPrice = expensiveBidding?.price,
        )
    }
}
