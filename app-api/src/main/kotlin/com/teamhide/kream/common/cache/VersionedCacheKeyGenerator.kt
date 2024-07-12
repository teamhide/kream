package com.teamhide.kream.common.cache

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.lang.reflect.Method

class VersionedCacheKeyGenerator(
    private val deployVersion: String,
) : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        val annotation = method.getAnnotation(VersionedCacheable::class.java)
        val keyExpression = annotation.key

        val parser = SpelExpressionParser()
        val context = StandardEvaluationContext()
        val paramNames = method.parameters.map { it.name }
        paramNames.forEachIndexed { index, name -> context.setVariable(name, params[index]) }

        val key = parser.parseExpression(keyExpression).getValue(context, String::class.java) ?: ""
        return "$key:$deployVersion"
    }
}
