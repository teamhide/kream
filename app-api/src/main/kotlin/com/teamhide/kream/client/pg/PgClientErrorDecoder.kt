package com.teamhide.kream.client.pg

import com.teamhide.kream.client.WebClientException
import feign.Response
import feign.codec.ErrorDecoder
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets

private val logger = KotlinLogging.logger { }

class PgClientErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        val statusCode = HttpStatus.valueOf(response.status())
        val body = parseBody(response = response)
        throw WebClientException(statusCode = statusCode, message = body)
    }

    private fun parseBody(response: Response): String {
        val body = response.body() ?: return ""

        if (body.length() == 0) {
            return ""
        }

        return try {
            body.asInputStream().use { inputStream ->
                String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
            }
        } catch (e: IOException) {
            logger.error { "PgClientErrorDecoder | parse body error. $e" }
            return e.message ?: ""
        }
    }
}
