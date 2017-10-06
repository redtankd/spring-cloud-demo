package rfq.service

import entity.QuoteRequest
import org.apache.ignite.IgniteCache
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * An implementation of {@link RequestForQuoteService} that will be deployed in the cluster.
 * </p>
 * The implementation stores RFQ's data in a dedicated distributed cache deployed on Data Nodes.
 */
@ResponseBody
@RequestMapping("/rfq")
class RequestForQuoteService(
        // The Ignite's native method to read/write to Cache
        val quoteRequestCache: IgniteCache<Int, QuoteRequest>
        // The Spring Data's method to read/write to Cache.
        // spring-data-commons:2.0.0.RC? is not compatible with Ignite 2.? now
        // val quoteRequestRepository: QuoteRequestRepository
) {
    @PostMapping("/quoteRequest/add/")
    fun addQuoteRequest(@RequestBody quoteRequest: QuoteRequest): Int {
        println("service: incoming Quote Request: $quoteRequest")

        val quoteRequestId = Random().nextInt()
        println("service: incoming Quote Request: id = $quoteRequestId")

        quoteRequestCache.put(quoteRequestId, quoteRequest)

        return quoteRequestId

//        quoteRequestRepository.save(quoteRequestId, quoteRequest)
    }

    @GetMapping("/quoteRequest/{quoteRequestId}")
    fun getQuoteRequest(@PathVariable quoteRequestId: Int): QuoteRequest {
        return quoteRequestCache.get(quoteRequestId)
    }

    @GetMapping("/quoteRequest/remove/{quoteRequestId}")
    fun removeQuoteRequest(quoteRequestId: Int) {
        quoteRequestCache.remove(quoteRequestId)
    }
}
