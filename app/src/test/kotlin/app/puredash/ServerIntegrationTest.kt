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
    fun statusEndpointReturnsOk() {
        val request = HttpRequest.newBuilder()
            .uri(URI("http://localhost:$testPort/status"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assertEquals("OK", response.body())
    }

    @Test
    fun rootPathReturnsOk() {
        val request = HttpRequest.newBuilder()
            .uri(URI("http://localhost:$testPort/"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
    }

    @Test
    fun arbitraryPathReturns404() {
        val request = HttpRequest.newBuilder()
            .uri(URI("http://localhost:$testPort/any/path"))
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(404, response.statusCode())
        assertEquals("Not Found", response.body())
    }
}

