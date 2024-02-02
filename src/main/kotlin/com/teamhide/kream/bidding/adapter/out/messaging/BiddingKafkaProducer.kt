package com.teamhide.kream.bidding.adapter.out.messaging

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class BiddingKafkaProducer<T>(
    private val kafkaTemplate: KafkaTemplate<String, T>,
) {
    fun send(topic: String, key: String, message: T) {
        val record = ProducerRecord(topic, key, message)
        kafkaTemplate.send(record)
            .whenComplete { result, ex ->
                if (ex == null) {
                    logger.info { "Sent message=[$message], offset=[${result.recordMetadata.offset()}]" }
                } else {
                    logger.error { "Unable to send message=[$message], message=${ex.message}" }
                }
            }
    }
}
