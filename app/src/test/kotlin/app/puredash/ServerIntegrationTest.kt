package app.puredash

import app.puredash.server.createServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.assertEquals

class ServerIntegrationTest {
    private val testPort = 18080
    private val server = createServer(testPort)
    private val httpClient = HttpClient.newBuilder().build()

    @BeforeEach
    fun setUp() {
        server.start()
    }

    @AfterEach
    fun tearDown() {
        server.stop(0)
    }

    @Test
    fun serverRespondsToRootPath() {
        val request = HttpRequest.newBuilder()
            .uri(URI("http://localhost:$testPort/"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assertEquals("text/html; charset=UTF-8", response.headers().firstValue("content-type").orElse(""))
        assert(response.body().contains("Hello World"))
    }

    @Test
    fun serverRespondsToArbitraryPath() {
        val request = HttpRequest.newBuilder()
            .uri(URI("http://localhost:$testPort/any/path"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assert(response.body().contains("Hello World"))
    }
}
