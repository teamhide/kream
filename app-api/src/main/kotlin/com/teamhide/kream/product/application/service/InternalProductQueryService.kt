package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.repository.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdQuery
import com.teamhide.kream.product.domain.usecase.GetProductByIdQuery
import com.teamhide.kream.product.domain.usecase.InternalProductQueryUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InternalProductQueryService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
    private val productRepositoryAdapter: ProductRepositoryAdapter,
) : InternalProductQueryUseCase {
    override fun getBiddingById(query: GetBiddingByIdQuery): Bidding {
        return biddingRepositoryAdapter.findById(biddingId = query.biddingId) ?: throw BiddingNotFoundException()
    }

    override fun getProductById(query: GetProductByIdQuery): Product? {
        return productRepositoryAdapter.findById(productId = query.productId) ?: throw ProductNotFoundException()
    }
}
