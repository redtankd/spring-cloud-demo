/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package rfq

import org.apache.ignite.IgniteException
import org.apache.ignite.IgniteSpringBean
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import rfq.service.Gateway

/**
 * A new Login Service Node will be started in a separate JVM process when this class gets executed.
 *
 * @param args Command line arguments, none required.
 * @throws IgniteException If failed.
 */
fun main(args: Array<String>) {
    SpringApplication(GatewayNode::class.java).apply {
        addInitializers(
                ApplicationContextInitializer<GenericApplicationContext> {
                    GatewayNode.beans().initialize(it)
                }
        )
    }.run(*args)
}

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties(GatewayConfig::class)
class GatewayNode {

    companion object {
        fun beans() = beans {

//            bean("igniteInstance") { igniteSpringBean() }

            bean<Gateway>()

            bean {
                router {
                    GET("/") {
                        val c = ref<GatewayConfig>()
                        ServerResponse.ok().body(BodyInserters.fromObject("Hello World. p1 = ${c.property1}, p2 = ${c.property2}"))
                    }
                }
            }
        }
    }

}

private fun igniteSpringBean(): IgniteSpringBean {
    val igniteConfig = IgniteConfiguration().apply {
        // Ignite configuration with all defaults
        // and enabled p2p deployment and enabled events.
        isPeerClassLoadingEnabled = true

        // This attribute is checked by common.GatewayFilter.
        userAttributes = mutableMapOf("gateway.node" to true)

        discoverySpi = cluster.discoverySpi()
    }

    return IgniteSpringBean().apply { configuration = igniteConfig }
}

@ConfigurationProperties(prefix = "gateway")
class GatewayConfig {
    var property1: Int = 0
    var property2: String = ""
}