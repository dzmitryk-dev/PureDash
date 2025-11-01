package app.puredash.server

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

fun createServer(port: Int): HttpServer {
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", port), 0)
    
    server.createContext("/") { exchange ->
        exchange.responseHeaders["Content-Type"] = "text/plain"
        val response = "OK"
        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.write(response.toByteArray())
        exchange.responseBody.close()
    }
    
    server.executor = null
    return server
}
