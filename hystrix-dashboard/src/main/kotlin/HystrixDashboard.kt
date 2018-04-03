package app

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
import org.springframework.cloud.netflix.turbine.EnableTurbine
import org.springframework.web.bind.annotation.RestController


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .web(WebApplicationType.SERVLET)
            .run(*args)
}

@SpringBootApplication
@EnableHystrixDashboard // confict with webflux
@EnableTurbine
@RestController
class Application