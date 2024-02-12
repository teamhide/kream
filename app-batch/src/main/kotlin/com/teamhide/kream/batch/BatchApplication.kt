package com.teamhide.kream.batch

import com.teamhide.kream.common.config.database.QuerydslConfig
import com.teamhide.kream.common.kafka.KafkaBaseProducer
import com.teamhide.kream.product.adapter.out.messaging.BiddingKafkaAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(basePackages = ["com.teamhide.kream.*.domain.model", "com.teamhide.kream.common.outbox"])
@EnableJpaRepositories(
    basePackages = [
        "com.teamhide.kream.*.adapter.out.persistence.jpa",
        "com.teamhide.kream.common.outbox",
    ],
    transactionManagerRef = "batchPlatformTransactionManager",
)
@Import(QuerydslConfig::class, BiddingKafkaAdapter::class, KafkaBaseProducer::class)
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}
