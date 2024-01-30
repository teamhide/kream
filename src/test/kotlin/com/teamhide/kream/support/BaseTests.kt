package com.teamhide.kream.support

import com.querydsl.jpa.impl.JPAQueryFactory
import com.teamhide.kream.common.config.database.DataSourceConfig
import com.teamhide.kream.common.config.database.SpringDataJpaConfig
import io.mockk.junit5.MockKExtension
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestExecutionListeners

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
annotation class TestEnvironment()

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@TestEnvironment
@ExtendWith(MockKExtension::class)
annotation class UnitTest

@TestConfiguration
class RepositoryTestConfig(
    @PersistenceContext private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@TestEnvironment
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(DataSourceConfig::class)
@Import(RepositoryTestConfig::class, SpringDataJpaConfig::class)
annotation class RepositoryTest

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@TestEnvironment
@DataMongoTest
annotation class MongoRepositoryTest

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@TestEnvironment
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(RestDocumentationExtension::class)
@TestExecutionListeners(
    value = [IntegrationTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
annotation class IntegrationTest
