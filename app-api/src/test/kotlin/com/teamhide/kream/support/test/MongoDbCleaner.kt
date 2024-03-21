package com.teamhide.kream.support.test

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.TestContextManager

@SpringBootTest
@TestEnvironment
class MongoDbCleaner : TestListener {
    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        val testContextManager = TestContextManager(this::class.java)
        testContextManager.prepareTestInstance(this)
        val mongoTemplate = testContextManager.testContext.applicationContext.getBean(MongoTemplate::class.java)
        val collections = getAllCollections(mongoTemplate = mongoTemplate)
        truncateAll(collections = collections, mongoTemplate = mongoTemplate)
    }

    private fun truncateAll(collections: Set<String>, mongoTemplate: MongoTemplate) {
        for (collection in collections) {
            mongoTemplate.remove(Query(), collection)
        }
    }

    private fun getAllCollections(mongoTemplate: MongoTemplate): Set<String> {
        return mongoTemplate.collectionNames
    }
}
