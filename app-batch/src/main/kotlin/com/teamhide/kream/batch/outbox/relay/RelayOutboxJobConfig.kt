package com.teamhide.kream.batch.outbox.relay

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RelayOutboxJobConfig(
    private val jobRepository: JobRepository,
    private val relayOutboxStep: Step,
) {
    companion object {
        const val JOB_NAME = "RELAY_OUTBOX_JOB"
    }

    @Bean(JOB_NAME)
    fun relayOutboxJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .start(relayOutboxStep)
            .build()
    }
}
