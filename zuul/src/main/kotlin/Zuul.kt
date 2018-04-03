package app

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
//            .web(WebApplicationType.SERVLET)
            .run(*args)
}

@SpringBootApplication
@EnableZuulProxy
class Application