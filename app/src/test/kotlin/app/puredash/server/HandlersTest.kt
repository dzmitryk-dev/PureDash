package app.puredash.server

import com.sun.net.httpserver.Headers
import com.sun.net.httpserver.HttpExchange
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.io.ByteArrayOutputStream
import java.net.URI
import kotlin.test.assertEquals

class HandlersTest {

    @Test
    fun htmlHandlerReturnsHtmlWithHelloWorld() {
        // Arrange
        val responseBody = ByteArrayOutputStream()
        val headers = Headers()
        val exchange: HttpExchange = mock {
            on { this.responseBody } doReturn responseBody
            on { responseHeaders } doReturn headers
            on { requestURI } doReturn URI("http://localhost:8080/")
        }
        val testHtmlContent = "<html>Test Content</html>"
        val handler = htmlHandler(htmlContentProvider = { testHtmlContent })

        // Act
        handler.invoke(exchange)

        // Assert
        verify(exchange).sendResponseHeaders(200, testHtmlContent.length.toLong())
        assertEquals(testHtmlContent, responseBody.toString())
    }

    @Test
    fun defaultOkHandlerReturnsOk() {
        // Arrange
        val responseBody = ByteArrayOutputStream()
        val headers = Headers()
        val exchange: HttpExchange = mock {
            on { this.responseBody } doReturn responseBody
            on { responseHeaders } doReturn headers
            on { requestURI } doReturn URI("http://localhost:8080/status")
        }
        val handler = defaultOkHandler

        // Act
        handler.invoke(exchange)

        // Assert
        verify(exchange).sendResponseHeaders(200, "OK".length.toLong())
        assertEquals("OK", responseBody.toString())
    }

    @Test
    fun defaultNotFoundHandlerReturns404() {
        // Arrange
        val responseBody = ByteArrayOutputStream()
        val headers = Headers()
        val exchange: HttpExchange = mock {
            on { this.responseBody } doReturn responseBody
            on { responseHeaders } doReturn headers
            on { requestURI } doReturn URI("http://localhost:8080/undefined")
        }
        val handler = defaultNotFoundHandler

        // Act
        handler.invoke(exchange)

        // Assert
        verify(exchange).sendResponseHeaders(404, "Not Found".length.toLong())
        assertEquals("Not Found", responseBody.toString())
    }
}

