package com.teamhide.kream.common.healthcheck.warmer

import com.teamhide.kream.common.exception.CustomException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.sql.SQLException
import javax.sql.DataSource

class DataSourceWarmerException : CustomException(
    statusCode = HttpStatus.SERVICE_UNAVAILABLE,
    errorCode = "HEALTH_CHECK_FAIL",
    message = ""
)

@Component
class DataSourceWarmer(
    @Qualifier("readerDataSource") private val readerDataSource: DataSource,
    @Qualifier("writerDataSource") private val writerDataSource: DataSource
) : WarmerTemplate() {
    override fun doRun() {
        checkConnection(readerDataSource)
        checkConnection(writerDataSource)
    }

    private fun checkConnection(dataSource: DataSource) {
        try {
            dataSource.connection.use { conn ->
                {
                    if (conn.isClosed) {
                        throw DataSourceWarmerException()
                    }
                    conn.prepareStatement("SELECT 1").executeQuery()
                }
            }
        } catch (e: SQLException) {
            throw DataSourceWarmerException()
        }
    }
}
