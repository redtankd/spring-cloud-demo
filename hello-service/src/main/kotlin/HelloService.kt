package app

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.InetAddress


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
}

@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
@RestController
@RefreshScope
class Application {

    @Autowired
    lateinit var environment: Environment

    @Value("\${test.bus.refresh}")
    lateinit var refresh: String

    @Autowired
    lateinit var helloSubClient: HelloSubClient

    @RequestMapping("/")
    fun home(): String {
        val addr = InetAddress.getLocalHost()

        return "HelloService at ${addr.hostAddress} with $refresh.<br><br>" + helloSubClient.helloMessage(addr.hostAddress)
    }

    @HystrixCommand(fallbackMethod = "error")
    @RequestMapping("/wrong")
    fun wrong(): String {
        throw RuntimeException()
    }

    fun error(): String {
        val addr = InetAddress.getLocalHost()

        return "HelloService Error at ${addr.hostAddress}"
    }

    @Bean
    fun handlerMapping(): HandlerMapping {
        val map = HashMap<String, WebSocketHandler>()
        map["/ws"] = MyWebSocketHandler()

        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = -1 // before annotated controllers
        return mapping
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }
}

class MyWebSocketHandler : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send(
                Flux.just(session.textMessage("Welcome"))
                        .concatWith(session
                                .receive()
                                .map { session.textMessage(it.payloadAsText) }
                                .doFinally {
                                    println("terminated! $it")
                                }
                        )
        )
    }
}

@FeignClient("HelloSubService")
interface HelloSubClient {

    @RequestMapping(method = [RequestMethod.GET], value = ["/{helloServiceAddr}"])
    fun helloMessage(@PathVariable("helloServiceAddr") helloServiceAddr: String): String

}