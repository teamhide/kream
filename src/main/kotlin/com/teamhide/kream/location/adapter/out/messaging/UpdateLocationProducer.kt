package com.teamhide.kream.location.adapter.out.messaging

import com.teamhide.kream.location.domain.model.UpdateUserLocation
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class UpdateLocationProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.topic.location-updated}")
    private val topicName: String,
) {

    fun send(key: String, message: UpdateUserLocation) {
        val record = ProducerRecord<String, Any>(topicName, key, message)
        kafkaTemplate.send(record)
            .whenComplete { result, ex ->
                if (ex == null) {
                    logger.info { "Sent message=[$message] with offset=[${result.recordMetadata.offset()}]" }
                } else {
                    logger.error { "Unable to send message=[$message] due to : ${ex.message}" }
                }
            }
    }
}
