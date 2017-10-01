package client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


fun main(args: Array<String>) {
    SpringApplication.run(GreetingApplication::class.java, *args)
}

@SpringBootApplication
@EnableDiscoveryClient
class GreetingApplication {


}


@Configuration
@EnableFeignClients
@EnableDiscoveryClient
class HelloWorldClient {

    @Autowired
    private val theClient: TheClient? = null

    @FeignClient(name = "HelloWorld")
    internal interface TheClient {

        @RequestMapping(path = arrayOf("/helloworld"), method = arrayOf(RequestMethod.GET))
        @ResponseBody
        fun helloWorld(): String
    }

    fun HelloWorld(): String {
        return theClient!!.helloWorld()
    }
}


@RestController
class GreetingController {

    @Autowired
    private val helloWorldClient: HelloWorldClient? = null

    @GetMapping("/get-greeting")
    fun greeting(): String {
        return helloWorldClient!!.HelloWorld()
    }
}