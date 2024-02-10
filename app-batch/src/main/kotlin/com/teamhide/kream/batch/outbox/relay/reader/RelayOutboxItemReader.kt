package com.teamhide.kream.batch.outbox.relay.reader

import com.teamhide.kream.batch.outbox.relay.step.RelayOutboxStepConfig
import com.teamhide.kream.common.outbox.Outbox
import com.teamhide.kream.common.outbox.OutboxRepository
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class RelayOutboxItemReader(
    private val outboxRepository: OutboxRepository,
) : AbstractPagingItemReader<Outbox>() {
    override fun doReadPage() {
        init()
        val pageRequest = PageRequest.of(0, RelayOutboxStepConfig.CHUNK_SIZE)
        results = outboxRepository.findAllBy(pageRequest)
    }

    protected fun init() {
        super.setPageSize(RelayOutboxStepConfig.CHUNK_SIZE)
        if (results == null) {
            results = arrayListOf()
            return
        }
        results.clear()
    }
}
