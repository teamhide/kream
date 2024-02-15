package com.teamhide.kream.common.kafka

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture

internal class KafkaBaseProducerTest : StringSpec({
    val kafkaTemplate = mockk<KafkaTemplate<String, String>>()
    val kafkaBaseProducer = KafkaBaseProducer(kafkaTemplate = kafkaTemplate)

    "카프카로 메시지를 전송 중 에러가 발생한다" {
        // Given
        every { kafkaTemplate.send(any<ProducerRecord<String, String>>()) } returns CompletableFuture.failedFuture(RuntimeException())

        // When, Then
        kafkaBaseProducer.send(topic = "topic", key = "key", message = "message")
    }

    "카프카로 메시지를 성공적으로 전송한다" {
        // Given
        val sendResult = mockk<SendResult<String, String>>()
        val recordMetadata = mockk<RecordMetadata>()
        every { sendResult.recordMetadata } returns recordMetadata
        every { recordMetadata.offset() } returns 1L
        every { kafkaTemplate.send(any<ProducerRecord<String, String>>()) } returns CompletableFuture.completedFuture(sendResult)

        // When, Then
        kafkaBaseProducer.send(topic = "topic", key = "key", message = "message")
    }
})
