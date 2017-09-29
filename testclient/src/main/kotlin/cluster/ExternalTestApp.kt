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
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.*

/**
 * Sample cluster that interacts with Gateway via plan socket API. The cluster doesn't use Apache Ignite API
 * at all and it's not connected to the cluster somehow.
 */

/**
 * Entry point.
 *
 * @param args Optional.
 */
fun main(args: Array<String>) = try {
    // Start the TestAppStartup before launching this example.
    for (vehicleId in 0..TOTAL_VEHICLES_NUMBER) {
        println(" >>> Getting maintenance schedule for vehicle:" + vehicleId)

        val command = QuoteRequestCommand(
                CommandAction.SUBMIT,
                30,
                QuoteRequest(
                        "abc",
                        Calendar.getInstance().time,
                        1.2
                )
        )

        val socket = Socket("127.0.0.1", 50000)

        val dos = ObjectOutputStream(socket.getOutputStream())

        dos.writeObject(command)

        val ois = ObjectInputStream(socket.getInputStream())

        val result = ois.readObject()

        if (result is QuoteRequestCommand)
            println("    >>>   " + ois)

        dos.close()
        ois.close()
        socket.close()
    }

    println(" >>> Shutting down the application.")
} catch (e: Exception) {
    e.printStackTrace()
}
