package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .run(*args)
}

@SpringBootApplication
@EnableZuulProxy
class Application