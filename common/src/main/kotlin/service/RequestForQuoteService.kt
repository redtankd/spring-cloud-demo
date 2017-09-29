package service

import entity.QuoteRequest
import org.apache.ignite.services.Service

/**
 * RequestForQuoteService Service interface which defines service specific methods that are visible to every cluster node that is
 * going to interact with the service using {@link IgniteServices} API. In general, the interface is not only used
 * by service implementations but also needed for the nodes that will talk to the service by means of service proxy -
 * {@link IgniteServices#serviceProxy(String, Class, boolean)}.
 */
interface RequestForQuoteService : Service {

    /**
     * Calls the service to add a new quoteRequest.
     *
     * @param quoteRequestId QuoteRequest unique ID.
     * @param quoteRequest QuoteRequest instance to add.
     */
    fun addQuoteRequest(quoteRequestId: Int, quoteRequest: QuoteRequest)

    /**
     * Calls the service to get details for a specific QuoteRequest.
     *
     * @param quoteRequestId QuoteRequest unique ID.
     */
    fun getQuoteRequest(quoteRequestId: Int): QuoteRequest

    /**
     * Calls the service to remove a specific vehicle.
     *
     * @param quoteRequestId QuoteRequest unique ID.
     */
    fun removeQuoteRequest(quoteRequestId: Int)
}
