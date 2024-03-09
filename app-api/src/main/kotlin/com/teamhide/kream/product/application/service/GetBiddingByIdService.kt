package com.teamhide.kream.product.application.service

import com.teamhide.kream.product.application.exception.BiddingNotFoundException
import com.teamhide.kream.product.domain.model.Bidding
import com.teamhide.kream.product.domain.repository.BiddingRepositoryAdapter
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdQuery
import com.teamhide.kream.product.domain.usecase.GetBiddingByIdUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetBiddingByIdService(
    private val biddingRepositoryAdapter: BiddingRepositoryAdapter,
) : GetBiddingByIdUseCase {
    override fun execute(query: GetBiddingByIdQuery): Bidding {
        return biddingRepositoryAdapter.findById(biddingId = query.biddingId) ?: throw BiddingNotFoundException()
    }
}
