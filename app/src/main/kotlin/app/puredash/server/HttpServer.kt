package app.puredash.server

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

fun createServer(port: Int): HttpServer {
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", port), 0)
    
    server.createContext("/") { exchange ->
        exchange.responseHeaders["Content-Type"] = "text/html; charset=UTF-8"
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
        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.write(response.toByteArray(Charsets.UTF_8))
        exchange.responseBody.close()
    }
    
    server.executor = null
    return server
}
