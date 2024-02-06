package com.teamhide.kream.common.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class AggregateTypeMapper private constructor() {
    companion object {
        val objectMapper = ObjectMapper().registerKotlinModule()

        inline fun <reified T : Any> from(payload: String): T {
            return objectMapper.readValue(payload, T::class.java)
        }
    }
}
