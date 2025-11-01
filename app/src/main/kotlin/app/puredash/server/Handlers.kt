package app.puredash.server

import com.sun.net.httpserver.HttpExchange

internal fun htmlHandler(): HttpExchange.() -> Unit =
    fun HttpExchange.() {
        this.responseHeaders["Content-Type"] = "text/html; charset=UTF-8"
        val response = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>PureDash</title>
        </head>
        <body>
            <h1>Hello World</h1>
        </body>
        </html>
    """.trimIndent()
        this.sendResponseHeaders(200, response.length.toLong())
        this.responseBody.write(response.toByteArray(Charsets.UTF_8))
        this.responseBody.close()
    }

internal val defaultOkHandler: HttpExchange.() -> Unit =
    fun HttpExchange.() {
        this.responseHeaders["Content-Type"] = "text/plain; charset=UTF-8"
        val response = "OK"
        this.sendResponseHeaders(200, response.length.toLong())
        this.responseBody.write(response.toByteArray(Charsets.UTF_8))
        this.responseBody.close()
    }

internal val defaultNotFoundHandler: HttpExchange.() -> Unit =
    fun HttpExchange.() {
        this.responseHeaders["Content-Type"] = "text/plain; charset=UTF-8"
        val response = "Not Found"
        this.sendResponseHeaders(404, response.length.toLong())
        this.responseBody.write(response.toByteArray(Charsets.UTF_8))
        this.responseBody.close()
    }