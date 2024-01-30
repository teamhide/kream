package com.teamhide.kream.common.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaTopicConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String,
) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = mapOf<String, Any>(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers)
        return KafkaAdmin(configs)
    }

    @Bean
    fun topics() = KafkaAdmin.NewTopics(
        TopicBuilder.name("locationUpdated")
            .partitions(3)
            .compact()
            .build()
    )
}
