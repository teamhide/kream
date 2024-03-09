package com.teamhide.kream.batch

import com.teamhide.kream.common.config.database.QuerydslConfig
import com.teamhide.kream.common.kafka.KafkaBaseProducer
import com.teamhide.kream.product.application.service.BiddingKafkaAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.MongoRepository

@SpringBootApplication
@EntityScan(basePackages = ["com.teamhide.kream.*.domain.model", "com.teamhide.kream.common.outbox"])
@EnableJpaRepositories(
    basePackages = [
        "com.teamhide.kream.*.domain.repository",
        "com.teamhide.kream.common.outbox",
    ],
    transactionManagerRef = "batchPlatformTransactionManager",
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            value = [MongoRepository::class]
        )
    ]
)
@Import(QuerydslConfig::class, BiddingKafkaAdapter::class, KafkaBaseProducer::class)
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}
