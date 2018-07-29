package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
}

@SpringBootApplication
@EnableCircuitBreaker
@RestController
@RefreshScope
class Application {

    @Autowired
    lateinit var environment: Environment


    @Value("\${test.bus.refresh}")
    lateinit var refresh: String

    @RequestMapping(method = [RequestMethod.GET], value = "/{helloServiceAddr}")
    fun helloMessage(@PathVariable("helloServiceAddr") helloServiceAddr: String): String {
        val addr = InetAddress.getLocalHost()

        return "HelloSubService at ${addr.hostAddress} called by HelloService at $helloServiceAddr. HelloSubService with $refresh."
    }

}
