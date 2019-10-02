package org.example

import com.hexagonkt.http.httpDate
import com.hexagonkt.http.server.Server
import com.hexagonkt.http.server.ServerPort
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager.bindObject
import java.time.LocalDateTime.now

val server: Server by lazy {
    Server {
        before {
            response.setHeader("Server", "Servlet/3.1")
            response.setHeader("Transfer-Encoding", "chunked")
            response.setHeader("Date", httpDate(now()))
        }

        get("/hello/") { ok("Hello, World!", "text/plain") }
    }
}

/**
 * Start the service from the command line.
 */
fun main() {
    bindObject<ServerPort>(JettyServletAdapter())
    server.start()
}

