package com.teamhide.kream.common.config

import com.teamhide.kream.common.cache.VersionedCacheKeyGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class RedisCacheConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,

    @Value("\${server.deploy-version}")
    private val deployVersion: String,
) {
    companion object {
        private const val GET_USER_LIST = "get_user_list"
    }

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            connectionFactory = redisConnectionFactory()
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
        }
    }

    @Bean
    fun redisMessageListener(): RedisMessageListenerContainer {
        return RedisMessageListenerContainer().apply {
            setConnectionFactory(redisConnectionFactory())
        }
    }

    @Bean
    fun versionedCacheKeyGenerator(): VersionedCacheKeyGenerator {
        return VersionedCacheKeyGenerator(deployVersion = deployVersion)
    }

    @Bean
    fun redisCacheManager(): RedisCacheManager {
        return RedisCacheManager.builder(redisConnectionFactory())
            .cacheDefaults(defaultCacheConfiguration())
            .withInitialCacheConfigurations(customCacheConfiguration())
            .build()
    }

    private fun defaultCacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer())
            )
            .entryTtl(Duration.ofSeconds(60))
    }

    private fun customCacheConfiguration(): Map<String, RedisCacheConfiguration> {
        return mapOf(
            GET_USER_LIST to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
        )
    }
}
