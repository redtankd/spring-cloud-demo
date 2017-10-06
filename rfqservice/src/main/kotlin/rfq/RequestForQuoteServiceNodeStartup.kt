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

import cluster.filters.RequestForQuoteServiceFilter
import entity.QuoteRequest
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.IgniteException
import org.apache.ignite.IgniteSpringBean
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import rfq.service.RequestForQuoteService


/**
 * A new RequestForQuote Service Node will be started in a separate JVM process.
 *
 * @param args Command line arguments, none required.
 * @throws IgniteException If failed.
 */
fun main(args: Array<String>) {
    SpringApplication(RequestForQuoteServiceNode::class.java).apply {
        addInitializers(
                ApplicationContextInitializer<GenericApplicationContext> {
                    RequestForQuoteServiceNode.beans().initialize(it)
                }
        )
    }.run(*args)
}

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableDiscoveryClient
//@EnableIgniteRepositories("rfq/repository")
@EnableConfigurationProperties(ServiceConfig::class)
class RequestForQuoteServiceNode {

    companion object {
        fun beans() = beans {
            bean("igniteInstance") { igniteBean() }

            bean<IgniteCache<Int, QuoteRequest>> { ref<Ignite>().cache("QuoteRequest") }

            bean { RequestForQuoteService(ref<ServiceConfig>().name, ref()) }

            bean {
                router {
                    GET("/") {
                        ok().body(fromObject("Hello World, this is root path"))
                    }
//                    "/rfq/quoteRequest".nest {
//                    val rfqService = ref<RequestForQuoteService>()
//                        GET("/{id}") { ok().body(fromObject(rfqService.getQuoteRequest(it.pathVariable("id").toInt()))) }
//                        GET("/add/{id}") {
//                            val quoteRequest = rfqService.addQuoteRequest(it.pathVariable("id").toInt())
//                            ok().body(fromObject(quoteRequest))
//                        }
//                    }
                }.filter { request, next ->
                    next.handle(request)
                }
            }
        }
    }
}

private fun igniteBean(): IgniteSpringBean {
    // Ignite configuration with all defaults
    // and enabled p2p deployment and enabled events.
    val igniteConfig = IgniteConfiguration().apply {
        isPeerClassLoadingEnabled = false

        /*
               Labeling QuoteRequest Service nodes with special attribute.
               This attribute is checked by common.RequestForQuoteServiceFilter.
               Due to the filter, the RequestForQuoteService might be deployed only on
               the nodes with this special attribute set.
             */
        userAttributes = mutableMapOf("vehicle.rfq.service.node" to true)

        // Configuring caches that will be deployed on Data Nodes
        setCacheConfiguration(
                // Cache for QuoteRequest
                CacheConfiguration<Int, QuoteRequest>().apply {
                    name = "QuoteRequest"
                    /*
                            Enabling a special nodes filter for the cache. The filter
                            will make sure that the cache will be deployed only on Data
                            Nodes, the nodes that have 'data.node' attribute in the local
                            node map.
                         */
                    nodeFilter = RequestForQuoteServiceFilter()
                    cacheMode = CacheMode.PARTITIONED
                    backups = 1
                }
        )

        discoverySpi = cluster.discoverySpi()
    }

    return IgniteSpringBean().apply { configuration = igniteConfig }
}

@ConfigurationProperties(prefix = "service")
class ServiceConfig {
    var name: String = ""
}