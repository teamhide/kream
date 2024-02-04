package com.teamhide.kream.common.config.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.teamhide.kream.*.adapter.out.persistence.mongo"])
@EnableMongoAuditing
class SpringDataMongoConfig : AbstractMongoClientConfiguration() {
    @Value("\${spring.data.mongodb.uri}")
    lateinit var mongoUri: String

    @Value("\${spring.data.mongodb.database}")
    lateinit var database: String

    override fun getDatabaseName(): String {
        return database
    }

    @Bean
    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)
        val settings = MongoClientSettings.builder()
            .apply {
                applyConnectionString(connectionString)
            }.build()
        return MongoClients.create(settings)
    }
}
