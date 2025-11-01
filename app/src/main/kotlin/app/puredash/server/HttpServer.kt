package app.puredash.server

import app.puredash.html.getHtmlContent
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.util.concurrent.Executor

internal class DefaultHttpHandler(
    private val okHandler: HttpExchange.() -> Unit = defaultOkHandler,
    private val notFoundHandler: HttpExchange.() -> Unit = defaultNotFoundHandler,
    private val htmlHandler: HttpExchange.() -> Unit = htmlHandler(
        htmlContentProvider = ::getHtmlContent
    ),
) : HttpHandler {

    override fun handle(exchange: HttpExchange) {
        val path = exchange.requestURI.path
        when (path) {
            "/status" -> okHandler(exchange)
            "/" -> htmlHandler(exchange)
            else -> notFoundHandler(exchange)
        }
    }
}

internal fun createServer(
    port: Int = 8080,
    executor: Executor? = null,
    rootHttpHandler: HttpHandler = DefaultHttpHandler(),
): HttpServer {
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", port), 0)

    server.createContext("/", rootHttpHandler)

    server.executor = executor
    return server
}
