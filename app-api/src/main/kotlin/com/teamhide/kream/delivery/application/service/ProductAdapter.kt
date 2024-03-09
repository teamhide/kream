package com.teamhide.kream.delivery.application.service

import com.teamhide.kream.delivery.domain.usecase.ProductExternalPort
import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdQuery
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdUseCase
import com.teamhide.kream.product.domain.usecase.GetProductByIdQuery
import com.teamhide.kream.product.domain.usecase.GetProductByIdUseCase
import org.springframework.stereotype.Component

@Component
class ProductAdapter(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getBiddingByIdUseCase: GetBiddingByIdUseCase,
) : ProductExternalPort {
    override fun findProductById(productId: Long): Product? {
        return try {
            GetProductByIdQuery(productId = productId).let {
                getProductByIdUseCase.execute(query = it)
            }
        } catch (e: ProductNotFoundException) {
            return null
        }
    }

    override fun findBiddingById(biddingId: Long): Bidding? {
        return try {
            GetBiddingByIdQuery(biddingId = biddingId).let {
                getBiddingByIdUseCase.execute(query = it)
            }
        } catch (e: BiddingNotFoundException) {
            return null
        }
    }
}
