package app.puredash

import app.puredash.server.createServer
import app.puredash.server.DefaultHttpHandler
import app.puredash.server.htmlHandler
import app.puredash.html.getHtmlContent
import org.slf4j.LoggerFactory
import puredash.plugin.clock.ClockWidget
import java.util.concurrent.Executors

fun main() {
    val logger = LoggerFactory.getLogger("app.puredash.Main")
    
    val widgets = listOf(
        ClockWidget()
    )
    
    val port = 8080
    val executor = Executors.newCachedThreadPool()
    val rootHttpHandler = DefaultHttpHandler(
        htmlHandler = htmlHandler { getHtmlContent(widgets) }
    )
    
    val server = createServer(
        port = port,
        executor = executor,
        rootHttpHandler = rootHttpHandler
    )
    server.start()
    logger.info("PureDash server started on port $port")
}