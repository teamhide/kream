package com.teamhide.kream.common.outbox

interface OutboxQuerydslRepository {
    fun completeByIds(outboxIds: List<Long>): Long
}
