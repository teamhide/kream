package com.teamhide.kream.batch.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.autoconfigure.batch.BatchDataSource
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(BatchProperties::class)
class BatchConfig {
    @Bean
    @BatchDataSource
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    fun batchDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean
    fun batchPlatformTransactionManager(emf: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(emf)
    }
}
