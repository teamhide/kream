package com.teamhide.kream.support.test

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContextManager
import java.sql.DatabaseMetaData
import java.sql.SQLException

@SpringBootTest
@TestEnvironment
class MysqlDbCleaner : TestListener {
    lateinit var jdbcTemplate: JdbcTemplate

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        val testContextManager = TestContextManager(this::class.java)
        testContextManager.prepareTestInstance(this)
        jdbcTemplate = testContextManager.testContext.applicationContext.getBean(JdbcTemplate::class.java)
        val tables = getAllTables(jdbcTemplate)
        truncateAll(tables = tables, jdbcTemplate = jdbcTemplate)
    }

    private fun truncateAll(tables: List<String>, jdbcTemplate: JdbcTemplate) {
        tables.forEach {
            table ->
            run {
                jdbcTemplate.execute("TRUNCATE TABLE $table")
            }
        }
    }

    private fun getAllTables(jdbcTemplate: JdbcTemplate): List<String> {
        try {
            jdbcTemplate.dataSource?.connection.use { connection ->
                val metaData: DatabaseMetaData = connection!!.metaData
                val tables: MutableList<String> = ArrayList()
                metaData.getTables(null, null, null, arrayOf("TABLE")).use { resultSet ->
                    while (resultSet.next()) {
                        tables.add(resultSet.getString("TABLE_NAME"))
                    }
                }
                return tables.filter { EXCLUDED_TABLES.contains(it).not() }
            }
        } catch (exception: SQLException) {
            throw IllegalStateException(exception)
        }
    }

    companion object {
        private val EXCLUDED_TABLES = setOf("flyway_schema_history")
    }
}
