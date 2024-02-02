package com.teamhide.kream.common.config.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.teamhide.kream.product.adapter.out.persistence.mongo"])
@EnableMongoAuditing
class SpringDataMongoConfig(
    @Value("\${spring.data.mongodb.uri}")
    private val mongoUri: String,
) {
    @Bean
    fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)
        val settings = MongoClientSettings.builder()
            .apply {
                applyConnectionString(connectionString)
            }.build()
        return MongoClients.create(settings)
    }
}
