package com.teamhide.kream.common.cache

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.lang.reflect.Method
import java.lang.reflect.Parameter

private const val DEPLOY_VERSION = "0.1.2"

class VersionedCacheKeyGeneratorTest : BehaviorSpec({
    val versionedKeyGenerator = VersionedCacheKeyGenerator(deployVersion = DEPLOY_VERSION)

    Given("버저닝 캐시 키 생성기에") {
        val target = mockk<Any>()
        val method = mockk<Method>()
        val annotation = VersionedCacheable(value = arrayOf("value"), key = "#parameterName")
        val parameter = mockk<Parameter>()

        every { parameter.name } returns "parameterName"
        every { method.getAnnotation(VersionedCacheable::class.java) } returns annotation
        every { method.parameters } returns arrayOf(parameter)

        When("키 생성을 요청하면") {
            val sut = versionedKeyGenerator.generate(target = target, method = method, params = arrayOf("test"))

            Then("버전 정보를 붙인 키를 만들어서 반환한다") {
                sut shouldBe "test:$DEPLOY_VERSION"
            }
        }
    }
})
