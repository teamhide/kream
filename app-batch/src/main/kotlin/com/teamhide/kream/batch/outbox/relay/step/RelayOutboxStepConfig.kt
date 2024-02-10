package com.teamhide.kream.batch.outbox.relay.step

import com.teamhide.kream.batch.outbox.relay.processor.RelayOutboxItemProcessor
import com.teamhide.kream.batch.outbox.relay.reader.RelayOutboxItemReader
import com.teamhide.kream.batch.outbox.relay.writer.RelayOutboxItemWriter
import com.teamhide.kream.common.outbox.Outbox
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class RelayOutboxStepConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val reader: RelayOutboxItemReader,
    private val processor: RelayOutboxItemProcessor,
    private val writer: RelayOutboxItemWriter,
) {
    companion object {
        const val STEP_NAME = "RELAY_OUTBOX_STEP"
        const val CHUNK_SIZE = 10
    }

    @Bean(STEP_NAME)
    @JobScope
    fun relayOutboxStep(): Step {
        return StepBuilder(STEP_NAME, jobRepository)
            .chunk<Outbox, Outbox>(CHUNK_SIZE, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }
}
