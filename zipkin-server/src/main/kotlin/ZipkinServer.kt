package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import zipkin.server.internal.EnableZipkinServer
import zipkin.server.internal.RegisterZipkinHealthIndicators


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .listeners(RegisterZipkinHealthIndicators())
            .run(*args)
}

@SpringBootApplication
@EnableZipkinServer
class Application