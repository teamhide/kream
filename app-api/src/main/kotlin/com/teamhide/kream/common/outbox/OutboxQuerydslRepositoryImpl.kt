package com.teamhide.kream.common.outbox

import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

class OutboxQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : OutboxQuerydslRepository {
    private val outbox = QOutbox.outbox

    override fun completeByIds(outboxIds: List<Long>): Long {
        return queryFactory
            .update(outbox)
            .set(outbox.completedAt, LocalDateTime.now())
            .where(outbox.id.`in`(*outboxIds.toTypedArray()))
            .execute()
    }
}
