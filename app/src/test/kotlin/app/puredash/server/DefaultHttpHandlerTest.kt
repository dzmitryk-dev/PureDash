package app.puredash.server

import com.sun.net.httpserver.Headers
import com.sun.net.httpserver.HttpExchange
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.io.ByteArrayOutputStream
import java.net.URI

class DefaultHttpHandlerTest {

    private val okHandler = mock<HttpExchange.() -> Unit>()
    private val notFoundHandler = mock<HttpExchange.() -> Unit>()
    private val htmlHandler = mock<HttpExchange.() -> Unit>()

    private val handler = DefaultHttpHandler(
        okHandler = okHandler,
        notFoundHandler = notFoundHandler,
        htmlHandler = htmlHandler
    )

    @Test
    fun routesToStatusEndpoint() {
        // Arrange
        val responseBody = ByteArrayOutputStream()
        val headers = Headers()
        val exchange: HttpExchange = mock {
            on { this.responseBody } doReturn responseBody
            on { responseHeaders } doReturn headers
            on { requestURI } doReturn URI("http://localhost:8080/status")
        }

        // Act
        handler.handle(exchange)

        // Assert
        verify(okHandler).invoke(exchange)
    }

    @Test
    fun routesToRootPath() {
        // Arrange
        val exchange: HttpExchange = mock {
            on { responseBody } doReturn ByteArrayOutputStream()
            on { responseHeaders } doReturn Headers()
            on { requestURI } doReturn URI("http://localhost:8080/")
        }

        // Act
        handler.handle(exchange)

        // Assert
        verify(htmlHandler).invoke(exchange)
    }

    @Test
    fun routesToNotFoundForUndefinedPath() {
        // Arrange
        val exchange: HttpExchange = mock {
            on { responseBody } doReturn ByteArrayOutputStream()
            on { responseHeaders } doReturn Headers()
            on { requestURI } doReturn URI("http://localhost:8080/undefined")
        }

        // Act
        handler.handle(exchange)

        // Assert
        verify(notFoundHandler).invoke(exchange)
    }

    @Test
    fun routesToNotFoundForArbitraryPath() {
        // Arrange
        val exchange: HttpExchange = mock {
            on { responseBody } doReturn ByteArrayOutputStream()
            on { responseHeaders } doReturn Headers()
            on { requestURI } doReturn URI("http://localhost:8080/any/path")
        }

        // Act
        handler.handle(exchange)

        // Assert
        verify(notFoundHandler).invoke(exchange)
    }
}