package com.teamhide.kream.product.domain.usecase

data class SaveOrUpdateProductDisplayCommand(val productId: Long, val price: Int)

interface SaveOrUpdateProductDisplayUseCase {
    fun execute(command: SaveOrUpdateProductDisplayCommand)
}
