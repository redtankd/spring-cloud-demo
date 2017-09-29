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

package cluster

import cluster.filters.RequestForQuoteServiceFilter
import org.apache.ignite.IgniteException
import org.apache.ignite.IgniteSpringBean
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.services.ServiceConfiguration
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import service.RequestForQuoteServiceImpl

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
@EnableIgniteRepositories("repository")
class RequestForQuoteServiceNode {

    companion object {
        fun beans() = beans {
            bean("igniteInstance") {
                // Ignite configuration with all defaults
                // and enabled p2p deployment and enabled events.
                val igniteConfig = IgniteConfiguration().apply {
                    isPeerClassLoadingEnabled = true

                    /*
                       Labeling QuoteRequest Service nodes with special attribute.
                       This attribute is checked by common.RequestForQuoteServiceFilter.
                       Due to the filter, the RequestForQuoteService might be deployed only on
                       the nodes with this special attribute set.
                     */
                    userAttributes = mutableMapOf("vehicle.service.node" to true)

                    setServiceConfiguration(
                            // Setting up RequestForQuoteService.
                            // The service will be deployed automatically
                            // according to the configuration below.
                            ServiceConfiguration().apply {
                                name = "RequestForQuoteService"
                                // Don't initialize service's properties here. Ignite's deployment
                                // will discard them.
                                service = RequestForQuoteServiceImpl()
                                // Only one instance of the service will be deployed cluster wide
                                totalCount = 1
                                // Only one instance of the service can be deployed on a single node.
                                maxPerNodeCount = 1
                                /*
                                  Enabling a special nodes filter for this service. The filter
                                  will make sure that the service will be deployed only on the
                                  nodes that have 'quoteRequest.service.node' attribute in the
                                  local node map.
                                 */
                                nodeFilter = RequestForQuoteServiceFilter()
                            }
                    )

                    discoverySpi = discoverySpi()
                }

                IgniteSpringBean().apply {
                    configuration = igniteConfig
                }
            }
        }
    }

}