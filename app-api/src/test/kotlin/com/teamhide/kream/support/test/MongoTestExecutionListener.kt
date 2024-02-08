package com.teamhide.kream.support.test

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class MongoTestExecutionListener : AbstractTestExecutionListener() {

    override fun afterTestMethod(testContext: TestContext) {
        val mongoTemplate = getMongoTemplate(testContext = testContext)
        val collections = getAllCollections(mongoTemplate = mongoTemplate)

        truncateAll(collections = collections, mongoTemplate = mongoTemplate)
    }

    private fun truncateAll(collections: Set<String>, mongoTemplate: MongoTemplate) {
        for (collection in collections) {
            mongoTemplate.remove(Query(), collection)
        }
    }

    private fun getMongoTemplate(testContext: TestContext): MongoTemplate {
        return testContext.applicationContext.getBean(MongoTemplate::class.java)
    }

    private fun getAllCollections(mongoTemplate: MongoTemplate): Set<String> {
        return mongoTemplate.collectionNames
    }
}
