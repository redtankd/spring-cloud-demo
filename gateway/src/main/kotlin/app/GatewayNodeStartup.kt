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

package app

import cluster.filters.GatewayFilter
import org.apache.ignite.IgniteException
import org.apache.ignite.IgniteSpringBean
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.services.ServiceConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.PropertyResolver
import service.GatewayImpl

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
class GatewayNode {

    companion object {
        fun beans() = beans {
            bean("igniteInstance") {
                // Ignite configuration with all defaults
                // and enabled p2p deployment and enabled events.
                val igniteConfig = IgniteConfiguration().apply {
                    isPeerClassLoadingEnabled = true

                    // This attribute is checked by common.GatewayFilter.
                    userAttributes = mutableMapOf("gateway.node" to true)

                    setServiceConfiguration(
                            // The service will be deployed automatically
                            // according to the configuration below.
                            ServiceConfiguration().apply {
                                name = ref<PropertyResolver>().getProperty("gateway.name") ?: "Gateway"
                                // Don't initialize service's properties here. Ignite's deployment
                                // will discard them.
                                service = GatewayImpl()
                                // Only one instance of the service will be deployed cluster wide
                                totalCount = 1
                                // Only one instance of the service can be deployed on a single node.
                                maxPerNodeCount = 1
                                /*
                                  Enabling a special nodes filter for this service. The filter
                                  will make sure that the service will be deployed only on the
                                  nodes that have 'maintenance.service.node' attribute in the
                                  local node map.
                                 */
                                nodeFilter = GatewayFilter()
                            }
                    )

                    discoverySpi = cluster.discoverySpi()
                }

                IgniteSpringBean().apply {
                    configuration = igniteConfig
                }
            }
        }
    }

}