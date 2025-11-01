package app.puredash

import com.sun.net.httpserver.HttpServer
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

fun main() {
    val logger = LoggerFactory.getLogger("app.puredash.Main")
    
    val port = 8080
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", port), 0)
    
    server.createContext("/") { exchange ->
        exchange.responseHeaders["Content-Type"] = "text/plain"
        val response = "OK"
        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.write(response.toByteArray())
        exchange.responseBody.close()
    }
    
    server.executor = null
    server.start()
    logger.info("PureDash server started on port $port")
}

