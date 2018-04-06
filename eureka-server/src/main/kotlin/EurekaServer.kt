package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .run(*args)
}

@SpringBootApplication
@EnableEurekaServer
class Application