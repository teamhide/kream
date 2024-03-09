package com.teamhide.kream.common.config.database

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.MongoRepository

@Configuration
@EnableJpaRepositories(
    basePackages = [
        "com.teamhide.kream.*.domain.repository",
        "com.teamhide.kream.common.outbox",
    ],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            value = [MongoRepository::class]
        )
    ]
)
class SpringDataJpaConfig
