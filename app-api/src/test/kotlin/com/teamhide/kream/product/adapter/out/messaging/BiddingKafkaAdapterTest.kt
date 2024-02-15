package com.teamhide.kream.product.adapter.out.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamhide.kream.product.domain.event.BiddingCompletedEvent
import com.teamhide.kream.product.domain.event.BiddingCreatedEvent
import com.teamhide.kream.product.domain.vo.BiddingType
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
import org.springframework.test.annotation.DirtiesContext

@EmbeddedKafka(
    partitions = 1,
    brokerProperties = ["listeners=PLAINTEXT://localhost:9092"],
    ports = [9092],
    topics = [
        "\${spring.kafka.topic.bidding-created}",
        "\${spring.kafka.topic.bidding-completed}",
    ]
)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class BiddingKafkaAdapterTest(
    @Value("\${spring.kafka.topic.bidding-created}")
    private val biddingCreatedTopic: String,

    @Value("\${spring.kafka.topic.bidding-completed}")
    private val biddingCompletedTopic: String,
) {
    @Autowired
    lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var biddingKafkaAdapter: BiddingKafkaAdapter

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
    fun `입찰 생성 메시지를 발송한다`() {
        // Given
        val message = BiddingCreatedEvent(
            productId = 1L, price = 2000, biddingType = BiddingType.SALE.name, biddingId = 1L
        )

        // When
        biddingKafkaAdapter.sendBiddingCreated(event = message)

        // Then
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, biddingCreatedTopic)
        val record = KafkaTestUtils.getSingleRecord(consumer, biddingCreatedTopic)
        val receivedMessage = objectMapper.readValue(record.value(), BiddingCreatedEvent::class.java)
        receivedMessage.productId shouldBe message.productId
        receivedMessage.biddingType shouldBe message.biddingType
        receivedMessage.price shouldBe message.price
    }

    @Test
    fun `입찰 종료 메시지를 발송한다`() {
        // Given
        val message = BiddingCompletedEvent(biddingId = 1L, productId = 1L)

        // When
        biddingKafkaAdapter.sendBiddingCompleted(event = message)

        // Then
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, biddingCompletedTopic)
        val record = KafkaTestUtils.getSingleRecord(consumer, biddingCompletedTopic)
        val receivedMessage = objectMapper.readValue(record.value(), BiddingCompletedEvent::class.java)
        receivedMessage.productId shouldBe message.productId
    }
}
