package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.domain.model.ProductDisplayRead
import com.teamhide.kream.product.domain.repository.ProductDisplayRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.GetProductsQuery
import com.teamhide.kream.product.domain.usecase.GetProductsUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetProductsService(
    private val productDisplayRepositoryAdapter: ProductDisplayRepositoryAdapter,
) : GetProductsUseCase {
    override fun execute(query: GetProductsQuery): List<ProductDisplayRead> {
        return productDisplayRepositoryAdapter.findAllBy(page = query.page, size = query.size)
            .map {
                ProductDisplayRead(
                    productId = it.productId,
                    name = it.name,
                    price = it.price,
                    brand = it.brand,
                    category = it.category,
                )
            }
    }
}
