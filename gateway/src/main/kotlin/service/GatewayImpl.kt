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
package service

import entity.Command
import entity.CommandAction
import entity.Login
import entity.QuoteRequestCommand
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteAtomicSequence
import org.apache.ignite.IgniteCache
import org.apache.ignite.resources.IgniteInstanceResource
import org.apache.ignite.resources.ServiceResource
import org.apache.ignite.services.ServiceContext
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket

/**
 * An implementation of {@link Gateway} that will be deployed in the cluster.
 * </p>
 * The implementation stores maintenance records in a dedicated distributed cache deployed on Data Nodes.
 */
class GatewayImpl : Gateway {

    @ServiceResource(
            serviceName = "RequestForQuoteService",
            proxyInterface = RequestForQuoteService::class
    )
    lateinit private var requestForQuoteService: RequestForQuoteService

    override fun executeCommand(command: Command) {
        when (command) {
            is QuoteRequestCommand -> if (command.action == CommandAction.SUBMIT) {
                requestForQuoteService.addQuoteRequest(command.id, command.QuoteRequest)
            }
            else -> return
        }
    }


    @IgniteInstanceResource
    lateinit private var ignite: Ignite

    /** Reference to the cache. */
    lateinit private var maintCache: IgniteCache<Int, Login>

    /** Login IDs generator */
    lateinit private var sequence: IgniteAtomicSequence

    /** Processor that accepts requests from external apps that don't use Apache Ignite API. */
    lateinit private var externalCallsProcessor: ExternalCallsProcessor

    /** {@inheritDoc} */
    override fun init(ctx: ServiceContext) {
        println("Initializing ${ctx.name()} Service on node:${ignite.cluster().localNode()}")

        /**
         * It's assumed that the cache has already been deployed. To do that, make sure to start Data Nodes with
         * a respective cache configuration.
         */
//        maintCache = ignite.cache("maintenance")

        /** Processor that accepts requests from external apps that don't use Apache Ignite API. */
        externalCallsProcessor = ExternalCallsProcessor()

        externalCallsProcessor.start()
    }

    /** {@inheritDoc} */
    override fun execute(ctx: ServiceContext) {
        println("Executing ${ctx.name()} Service on node:" + ignite.cluster().localNode())

        /**
         * Getting the sequence that will be used for IDs generation.
         */
        sequence = ignite.atomicSequence("MaintenanceIds", 1, true)
    }

    /** {@inheritDoc} */
    override fun cancel(ctx: ServiceContext) {
        println("Stopping ${ctx.name()} Service on node:" + ignite.cluster().localNode())

        // Stopping external requests processor.
        externalCallsProcessor.interrupt()
    }

    /**
     * Thread that accepts request from external applications that don't use Apache Ignite service grid API.
     */
    private inner class ExternalCallsProcessor : Thread() {
        /** Server socket to accept external connections. */
        lateinit private var externalConnect: ServerSocket

        /** {@inheritDoc} */
        @Override override fun run() {
            try {
                externalConnect = ServerSocket(50000)

                while (!isInterrupted) {
                    val socket = externalConnect.accept()

                    val dis = ObjectInputStream(socket.getInputStream())

                    // Getting userId.
                    val obj = dis.readObject()

                    if (obj is Command) {
                        executeCommand(obj)

                        val dos = ObjectOutputStream(socket.getOutputStream())

                        // Writing the result into the socket.
                        dos.writeObject(obj)

                        dos.close()
                    }

                    dis.close()

                    socket.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        /** {@inheritDoc} */
        @Override override fun interrupt() {
            super.interrupt()

            try {
                externalConnect.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
