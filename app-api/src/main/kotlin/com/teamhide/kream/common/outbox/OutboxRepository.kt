package com.teamhide.kream.common.outbox

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OutboxRepository : JpaRepository<Outbox, Long>, OutboxQuerydslRepository {
    @Query("SELECT o.* FROM outbox o WHERE o.completed_at IS NULL FOR UPDATE SKIP LOCKED", nativeQuery = true)
    fun findAllBy(pageable: Pageable): List<Outbox>
}
