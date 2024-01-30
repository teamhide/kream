package com.teamhide.kream.location.adapter.out.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.location.domain.model.UpdateUserLocation
import com.teamhide.kream.location.domain.vo.LocationRequestType
import io.kotest.matchers.shouldBe
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils

@EmbeddedKafka(
    partitions = 1,
    brokerProperties = ["listeners=PLAINTEXT://localhost:9092"],
    ports = [9092],
    topics = ["location-updated"]
)
@SpringBootTest
class UpdateLocationProducerTest(
    @Value("\${spring.kafka.topic.location-updated}")
    private val topicName: String,
) {
    @Autowired
    lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var updateLocationProducer: UpdateLocationProducer

    lateinit var consumer: Consumer<String, String>

    @BeforeEach
    fun setUp() {
        embeddedKafkaBroker.afterPropertiesSet()
        val configs = KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker)
        consumer = DefaultKafkaConsumerFactory(configs, StringDeserializer(), StringDeserializer()).createConsumer()
    }

    @AfterEach
    fun tearDown() {
        consumer.close()
        embeddedKafkaBroker.destroy()
    }

    @Test
    fun `유저 위치 업데이트 메시지를 발송한다`() {
        // Given
        val message = UpdateUserLocation(type = LocationRequestType.UPDATE, userId = 1L, lat = 1.1, lng = 2.2)

        // When
        updateLocationProducer.send(key = message.userId.toString(), message = message)

        // Then
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topicName)
        val record = KafkaTestUtils.getSingleRecord(consumer, topicName)
        val receivedMessage = objectMapper.readValue(record.value(), UpdateUserLocation::class.java)
        receivedMessage.type shouldBe message.type
        receivedMessage.userId shouldBe message.userId
        receivedMessage.lat shouldBe message.lat
        receivedMessage.lng shouldBe message.lng
    }
}
