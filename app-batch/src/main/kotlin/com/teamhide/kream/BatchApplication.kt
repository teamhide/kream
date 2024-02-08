package com.teamhide.kream

import com.teamhide.kream.common.config.database.QuerydslConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
@EntityScan(basePackages = ["com.teamhide.kream.*.adapter.out.persistence.jpa"])
@Import(QuerydslConfig::class)
@ComponentScan(basePackages = ["com.teamhide.kream.batch"])
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}
