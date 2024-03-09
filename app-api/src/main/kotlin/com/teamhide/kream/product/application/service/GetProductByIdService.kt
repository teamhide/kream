package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.ProductNotFoundException
import com.teamhide.kream.product.domain.model.Product
import com.teamhide.kream.product.domain.repository.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.GetProductByIdQuery
import com.teamhide.kream.product.domain.usecase.GetProductByIdUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetProductByIdService(
    private val productRepositoryAdapter: ProductRepositoryAdapter,
) : GetProductByIdUseCase {
    override fun execute(query: GetProductByIdQuery): Product {
        return productRepositoryAdapter.findById(productId = query.productId) ?: throw ProductNotFoundException()
    }
}
