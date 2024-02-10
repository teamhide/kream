package com.teamhide.kream.common.outbox

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "outbox")
class Outbox(
    @Column(name = "aggregate_type", length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    val aggregateType: AggregateType,

    @Column(name = "payload", columnDefinition = "TEXT", nullable = false)
    val payload: String,

    @Column(name = "completed_at", nullable = true)
    var completedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    fun complete() {
        this.completedAt = LocalDateTime.now()
    }
}
