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

import entity.CommandAction
import entity.QuoteRequest
import entity.QuoteRequestCommand
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteException
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import service.Gateway
import service.RequestForQuoteService
import java.util.*

/**
 * Dummy vehicles' names
 */
val VEHICLES_NAMES = listOf("TOYOTA", "BMW", "MERCEDES", "HYUNDAI", "FORD")

/**
 * Total number of vehicles.
 */
val TOTAL_VEHICLES_NUMBER = 10

/**
 * Start up a testing application that connects to the cluster using an Ignite client node.
 * <p>
 * The cluster will fill in the caches with sample data and call the service.
 *
 * @param args Command line arguments, none required.
 * @throws IgniteException If failed.
 */
fun main(args: Array<String>) {
    val context = SpringApplication(TestClientApplication::class.java).apply {
        addInitializers(
                ApplicationContextInitializer<GenericApplicationContext> {
                    TestClientApplication.beans().initialize(it)
                }
        )
    }.run(*args)


    val ignite = context.getBean(Ignite::class.java)

    val gateway = ignite.services().serviceProxy("Gateway",
            Gateway::class.java, false)

    val s1 = ignite.services().serviceProxy("RequestForQuoteService",
            RequestForQuoteService::class.java, false)
    s1.addQuoteRequest(20, QuoteRequest(
            "abc",
            Calendar.getInstance().time,
            1.2
    ))

    val rand = Random()

    val calendar = Calendar.getInstance()

    // submitting QuoteRequest.
    for (i in 0..TOTAL_VEHICLES_NUMBER) {

        calendar.set(Calendar.MONTH, rand.nextInt(12))
        calendar.set(Calendar.YEAR, 2000 + rand.nextInt(17))

        val quoteRequest = QuoteRequest(
                VEHICLES_NAMES[rand.nextInt(VEHICLES_NAMES.size)],
                calendar.time,
                (11000 + rand.nextInt(10000)).toDouble()
        )

        gateway.executeCommand(QuoteRequestCommand(CommandAction.SUBMIT, i, quoteRequest))
    }

    val cache = ignite.cache<Int, QuoteRequest>("QuoteRequest")
    cache.forEach {
        println("key = ${it.key} value = ${it.value}")
    }

    context.close()
}

@SpringBootConfiguration
class TestClientApplication {

    companion object {

        fun beans() = beans {
            bean {
                val igniteConfig = IgniteConfiguration().apply {
                    isClientMode = true
                    isPeerClassLoadingEnabled = true
                    discoverySpi = discoverySpi()
                }
                Ignition.start(igniteConfig)
            }
        }
    }
}
