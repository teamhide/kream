package com.teamhide.kream.common.filter

import com.teamhide.kream.test.UnitTest
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.Exception

@UnitTest
@ExtendWith(OutputCaptureExtension::class)
internal class RequestLogFilterTest {
    private val requestMock = mockk<ContentCachingRequestWrapper>()
    private val responseMock = mockk<ContentCachingResponseWrapper>()

    @Test
    fun `logRequest - Request body가 없는 경우 body없이 로깅된다`(output: CapturedOutput) {
        // Given
        val returnValue = "".toByteArray()
        every { requestMock.contentAsByteArray } returns returnValue
        every { requestMock.method } returns HttpMethod.POST.name()
        every { requestMock.requestURI } returns "URI"
        every { requestMock.queryString } returns ""

        // When
        RequestLogFilter.logRequest(requestMock)

        // Then
        assertThat(output.all).contains("Request | POST URI")
    }

    @Test
    fun `logRequest - Request body가 있는 경우 포함하여 출력한다`(output: CapturedOutput) {
        // Given
        val returnValue = "abc".toByteArray()
        every { requestMock.contentAsByteArray } returns returnValue
        every { requestMock.method } returns HttpMethod.POST.name()
        every { requestMock.requestURI } returns "URI"
        every { requestMock.queryString } returns "id=hide"

        // When
        RequestLogFilter.logRequest(requestMock)

        // Then
        assertThat(output.all).contains("Request | POST URI?id=hide | body = abc")
    }

    @Test
    fun `logResponse - 응답 코드가 500이 아니고 Response body가 없는 경우 body는 제외하고 출력한다`(output: CapturedOutput) {
        // Given
        every { responseMock.contentInputStream } returns InputStream.nullInputStream()
        every { requestMock.method } returns HttpMethod.POST.name()
        every { requestMock.requestURI } returns "URI"
        every { requestMock.queryString } returns "id=hide"
        every { responseMock.status } returns HttpStatus.OK.value()
        every { responseMock.copyBodyToResponse() } returns Unit

        // When
        RequestLogFilter.logResponse(requestMock, responseMock)

        // Then
        assertThat(output.all).contains("Response | POST URI?id=hide | 200")
    }

    @Test
    fun `logResponse - 응답 코드가 500이 아니고 Response body가 있는 경우 body를 포함하여 출력한다`(output: CapturedOutput) {
        // Given
        val returnValue = "abc".toByteArray()
        every { responseMock.contentInputStream } returns ByteArrayInputStream(returnValue)
        every { requestMock.method } returns HttpMethod.POST.name()
        every { requestMock.requestURI } returns "URI"
        every { requestMock.queryString } returns "id=hide"
        every { responseMock.status } returns HttpStatus.OK.value()
        every { responseMock.copyBodyToResponse() } returns Unit

        // When
        RequestLogFilter.logResponse(requestMock, responseMock)

        // Then
        assertThat(output.all).contains("Response | POST URI?id=hide | 200 | body = abc")
    }

    @Test
    fun `logResponse - 응답 코드가 500이고 Response body가 없는 경우 body는 제외하고 출력한다`(output: CapturedOutput) {
        // Given
        every { responseMock.contentInputStream } returns InputStream.nullInputStream()
        every { requestMock.method } returns HttpMethod.POST.name()
        every { requestMock.requestURI } returns "URI"
        every { requestMock.queryString } returns ""
        every { responseMock.status } returns HttpStatus.INTERNAL_SERVER_ERROR.value()
        every { responseMock.copyBodyToResponse() } returns Unit

        // When
        RequestLogFilter.logResponse(requestMock, responseMock)

        // Then
        assertThat(output.all).contains("Response | POST URI | 500")
    }

    @Test
    fun `logResponse - 응답 코드가 500이고 Response body가 있는 경우 body를 포함하여 출력한다`(output: CapturedOutput) {
        // Given
        val returnValue = "abc".toByteArray()
        every { responseMock.contentInputStream } returns ByteArrayInputStream(returnValue)
        every { requestMock.method } returns HttpMethod.POST.name()
        every { requestMock.requestURI } returns "URI"
        every { requestMock.queryString } returns "id=hide"
        every { responseMock.status } returns HttpStatus.INTERNAL_SERVER_ERROR.value()
        every { responseMock.copyBodyToResponse() } returns Unit

        // When
        RequestLogFilter.logResponse(requestMock, responseMock)

        // Then
        assertThat(output.all).contains("Response | POST URI?id=hide | 500 | body = abc")
    }

    @Test
    fun `getRequestBody - Request body가 없는 경우 빈값을 리턴한다`() {
        // Given
        val returnValue = "".toByteArray()
        every { requestMock.contentAsByteArray } returns returnValue

        // When
        val sut = RequestLogFilter.getRequestBody(requestMock)

        // Then
        assertThat(sut.length).isEqualTo(0)
    }

    @Test
    fun `getRequestBody - Exception이 발생하는 경우 빈값을 리턴한다`() {
        // Given
        every { requestMock.contentAsByteArray } throws Exception()

        // When
        val sut = RequestLogFilter.getRequestBody(requestMock)

        // Then
        assertThat(sut.length).isEqualTo(0)
    }

    @Test
    fun `getRequestBody - Request body가 있는 경우 해당 값을 리턴한다`() {
        // Given
        val returnValue = "abc".toByteArray()
        every { requestMock.contentAsByteArray } returns returnValue

        // When
        val sut = RequestLogFilter.getRequestBody(requestMock)

        // Then
        assertThat(sut).isEqualTo("abc")
    }

    @Test
    fun `getResponseBody - Response body가 없는 경우 빈값을 리턴한다`() {
        // Given
        every { responseMock.contentInputStream } returns InputStream.nullInputStream()

        // When
        val sut = RequestLogFilter.getResponseBody(responseMock)

        // Then
        assertThat(sut.length).isEqualTo(0)
    }

    @Test
    fun `getResponseBody - Exception이 발생하는 경우 빈값을 리턴한다`() {
        // Given
        every { responseMock.contentInputStream } throws Exception()

        // When
        val sut = RequestLogFilter.getResponseBody(responseMock)

        // Then
        assertThat(sut.length).isEqualTo(0)
    }

    @Test
    fun `getResponseBody - Response body가 있는 경우 해당 값을 리턴한다`() {
        // Given
        val returnValue = "abc".toByteArray()
        every { responseMock.contentInputStream } returns ByteArrayInputStream(returnValue)
        every { responseMock.copyBodyToResponse() } returns Unit

        // When
        val sut = RequestLogFilter.getResponseBody(responseMock)

        // Then
        assertThat(sut).isEqualTo("abc")
    }
}
