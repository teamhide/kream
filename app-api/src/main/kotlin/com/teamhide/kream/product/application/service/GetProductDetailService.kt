package com.teamhide.kream.product.application.service

import com.teamhide.kream.bidding.adapter.out.persistence.BiddingRepositoryAdapter
import com.teamhide.kream.bidding.domain.vo.BiddingType
import com.teamhide.kream.product.adapter.out.persistence.ProductRepositoryAdapter
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.model.ProductDetail
import com.teamhide.kream.product.domain.usecase.GetProductDetailQuery
import com.teamhide.kream.product.domain.usecase.GetProductDetailUseCase
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
            productId = product.productId, biddingType = BiddingType.SALE
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
