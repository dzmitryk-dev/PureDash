package app.puredash

import app.puredash.server.createServer
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

fun main() {
    val logger = LoggerFactory.getLogger("app.puredash.Main")
    
    val port = 8080
    val server = createServer(
        port = port,
        executor = Executors.newCachedThreadPool()
    )
    server.start()
    logger.info("PureDash server started on port $port")
}

