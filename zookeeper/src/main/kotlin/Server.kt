package server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@EnableDiscoveryClient
@RestController
class Server {

    @RequestMapping("/")
    fun home(): String {
        return "Hello World"
    }

    @GetMapping("/helloworld")
    fun helloWorld(): String {
        println("---------ok")
        return "Hello World!"
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Server::class.java, *args)
}