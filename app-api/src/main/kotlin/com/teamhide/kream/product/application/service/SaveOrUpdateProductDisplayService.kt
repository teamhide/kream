package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.domain.model.ProductDisplay
import com.teamhide.kream.product.domain.repository.ProductDisplayRepositoryAdapter
import com.teamhide.kream.product.domain.repository.ProductRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayCommand
import com.teamhide.kream.product.domain.usecase.SaveOrUpdateProductDisplayUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SaveOrUpdateProductDisplayService(
    private val productDisplayRepositoryAdapter: ProductDisplayRepositoryAdapter,
    private val productRepositoryAdapter: ProductRepositoryAdapter,
) : SaveOrUpdateProductDisplayUseCase {
    override fun execute(command: SaveOrUpdateProductDisplayCommand) {
        val existProductDisplay = productDisplayRepositoryAdapter.findByProductId(productId = command.productId)

        if (existProductDisplay == null) {
            val product = productRepositoryAdapter.findById(productId = command.productId) ?: return
            val productDisplay = ProductDisplay(
                productId = product.id,
                name = product.name,
                price = command.price,
                brand = product.productBrand.name,
                category = product.productCategory.name,
                lastBiddingId = command.biddingId,
            )
            productDisplayRepositoryAdapter.save(productDisplay = productDisplay)
            return
        }
        if (existProductDisplay.price != 0 && existProductDisplay.price < command.price) {
            return
        }
        existProductDisplay.changePrice(price = command.price)
        existProductDisplay.changeLastBiddingId(biddingId = command.biddingId)
        productDisplayRepositoryAdapter.save(productDisplay = existProductDisplay)
    }
}
