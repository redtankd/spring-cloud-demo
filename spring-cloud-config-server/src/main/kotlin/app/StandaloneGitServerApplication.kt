package app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class StandaloneGitServerApplication

fun main(args: Array<String>) {
    SpringApplication.run(StandaloneGitServerApplication::class.java, *args)
}