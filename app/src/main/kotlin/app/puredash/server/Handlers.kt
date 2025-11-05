package app.puredash.server

import com.sun.net.httpserver.HttpExchange
import java.io.OutputStream

internal fun htmlHandler(
    htmlContentProvider: () -> String
): HttpExchange.() -> Unit =
    fun HttpExchange.() {
        this.responseHeaders["Content-Type"] = "text/html; charset=UTF-8"
        val response = htmlContentProvider()
        this.sendResponseHeaders(200, response.length.toLong())
        this.responseBody.writeData(response)
    }

internal val defaultOkHandler: HttpExchange.() -> Unit =
    fun HttpExchange.() {
        this.responseHeaders["Content-Type"] = "text/plain; charset=UTF-8"
        val response = "OK"
        this.sendResponseHeaders(200, response.length.toLong())
        this.responseBody.writeData(response)
    }

internal val defaultNotFoundHandler: HttpExchange.() -> Unit =
    fun HttpExchange.() {
        this.responseHeaders["Content-Type"] = "text/plain; charset=UTF-8"
        val response = "Not Found"
        this.sendResponseHeaders(404, response.length.toLong())
        this.responseBody.writeData(response)
    }

internal fun responseNotFound(exchange: HttpExchange) {
    defaultNotFoundHandler(exchange)
}

fun OutputStream.writeData(data: String) {
    this.use { out ->
        out.write(data.toByteArray(Charsets.UTF_8))
    }
}