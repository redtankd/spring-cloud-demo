package app

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
}

@SpringBootApplication
@RestController
class Application {

    @RequestMapping("/")
    fun home(): String {
        return "Hello world at gateway"
    }

}