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
package rfq.service

import entity.QuoteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * An implementation of {@link Gateway} that will be deployed in the cluster.
 * </p>
 * The implementation stores maintenance records in a dedicated distributed cache deployed on Data Nodes.
 */
@RequestMapping
@ResponseBody
class Gateway {

    @GetMapping("/add")
    fun executeCommand(): Int {
        println("-----------add")

        val quoteRequest = QuoteRequest("abc", Calendar.getInstance().time, 1.2)

        return rfqService!!.addQuoteRequest(quoteRequest)
//        when (command) {
//            is QuoteRequestCommand -> if (command.action == CommandAction.SUBMIT) {
//                requestForQuoteService.addQuoteRequest(command.id, command.QuoteRequest)
//            }
//            else -> return
//        }
    }

    @Autowired
    private val rfqService: RequestForQuoteServiceClient? = null

    @FeignClient(name = "RequestForQuoteServiceNode", path = "/rfq")
    internal interface RequestForQuoteServiceClient {

        @RequestMapping(
                path = arrayOf("/quoteRequest/add/"),
                method = arrayOf(RequestMethod.POST)
        )
        @ResponseBody
        fun addQuoteRequest(@RequestBody quoteRequest: QuoteRequest): Int
    }


    /** Reference to the cache. */
//    lateinit private var maintCache: IgniteCache<Int, Login>

    /** Login IDs generator */
//    lateinit private var sequence: IgniteAtomicSequence


}
