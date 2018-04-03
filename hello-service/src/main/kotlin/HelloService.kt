package app

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
}

@SpringBootApplication
@EnableCircuitBreaker
@RestController
class Application {

    @Autowired
    lateinit var environment: Environment

    @RequestMapping("/")
    fun home(): String {
        val port = environment.getProperty("local.server.port")
        return "Hello world at $port"
    }

    @HystrixCommand(fallbackMethod = "error")
    @RequestMapping("/wrong1")
    fun wrong1(): String {
        throw RuntimeException()
    }

    @HystrixCommand(fallbackMethod = "error")
    @RequestMapping("/wrong2")
    fun wrong2(): String {
        throw RuntimeException()
    }

    fun error(): String {
        val port = environment.getProperty("local.server.port")
        return "Error at $port"
    }
}