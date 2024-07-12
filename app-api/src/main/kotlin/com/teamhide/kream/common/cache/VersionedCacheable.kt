package com.teamhide.kream.common.cache

import org.springframework.cache.annotation.Cacheable
import org.springframework.core.annotation.AliasFor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Cacheable(keyGenerator = "versionedCacheKeyGenerator")
annotation class VersionedCacheable(
    @get:AliasFor(annotation = Cacheable::class, attribute = "value")
    val value: Array<String> = [],

    @get:AliasFor(annotation = Cacheable::class, attribute = "cacheNames")
    val cacheNames: Array<String> = [],

    @get:AliasFor(annotation = Cacheable::class, attribute = "key")
    val keyName: String = "",

    val key: String,

    @get:AliasFor(annotation = Cacheable::class, attribute = "cacheManager")
    val cacheManager: String = "",

    @get:AliasFor(annotation = Cacheable::class, attribute = "cacheResolver")
    val cacheResolver: String = "",

    @get:AliasFor(annotation = Cacheable::class, attribute = "condition")
    val condition: String = "",

    @get:AliasFor(annotation = Cacheable::class, attribute = "unless")
    val unless: String = "",

    @get:AliasFor(annotation = Cacheable::class, attribute = "sync")
    val sync: Boolean = false
)
