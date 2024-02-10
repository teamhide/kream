package com.teamhide.kream.batch.outbox.relay.writer

import com.teamhide.kream.common.outbox.Outbox
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class RelayOutboxItemWriter : ItemWriter<Outbox> {
    override fun write(chunk: Chunk<out Outbox>) {
        for (outbox in chunk) {
            outbox.complete()
        }
    }
}
