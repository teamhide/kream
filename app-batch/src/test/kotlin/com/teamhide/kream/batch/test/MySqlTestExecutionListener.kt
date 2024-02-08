package com.teamhide.kream.batch.test

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import java.sql.DatabaseMetaData
import java.sql.SQLException

class MySqlTestExecutionListener : AbstractTestExecutionListener() {

    override fun afterTestMethod(testContext: TestContext) {
        val jdbcTemplate = getJdbcTemplate(testContext)
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

    private fun getJdbcTemplate(testContext: TestContext): JdbcTemplate {
        return testContext.applicationContext.getBean(JdbcTemplate::class.java)
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
        private val EXCLUDED_TABLES = setOf(
            "flyway_schema_history",
            "BATCH_JOB_EXECUTION",
            "BATCH_JOB_EXECUTION_CONTEXT",
            "BATCH_JOB_EXECUTION_PARAMS",
            "BATCH_JOB_EXECUTION_SEQ",
            "BATCH_JOB_INSTANCE",
            "BATCH_JOB_SEQ",
            "BATCH_STEP_EXECUTION",
            "BATCH_STEP_EXECUTION_CONTEXT",
            "BATCH_STEP_EXECUTION_SEQ"
        )
    }
}
