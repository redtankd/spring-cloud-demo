package service

import entity.QuoteRequest
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.resources.IgniteInstanceResource
import org.apache.ignite.resources.SpringApplicationContextResource
import org.apache.ignite.services.ServiceContext
import org.springframework.context.ApplicationContext
import repository.QuoteRequestRepository

/**
 * An implementation of {@link RequestForQuoteService} that will be deployed in the cluster.
 * </p>
 * The implementation stores RFQ's data in a dedicated distributed cache deployed on Data Nodes.
 */
class RequestForQuoteServiceImpl : RequestForQuoteService {

    /*
        The Spring Data's method to read/write to Cache.

        Because RequestForQuoteServiceImpl is registered as Ignite's service,
        which's dependencies MUST be injected by Ignite.

        @Transient @SpringResource(resourceClass = QuoteRequestRepository::class) doesn't
        work. The injection will block the Ignite node's bootstrap.

        So I have to injecting QuoteRequestRepository manually with ApplicationContext in
        service's execute() method. Injection in init() method will block the Ignite node's
        bootstrap too. It means that the Spring's bean should be injected when service is
        deployed.
     */
    private lateinit var quoteRequestRepository: QuoteRequestRepository

    @SpringApplicationContextResource
    private lateinit var applicationContext: ApplicationContext

    /*
        The Ignite's native method to read/write to Cache
     */
    @IgniteInstanceResource
    lateinit var ignite: Ignite

    /** Reference to the cache. */
    lateinit private var quoteRequestCache: IgniteCache<Int, QuoteRequest>

    /** {@inheritDoc} */
    override fun init(ctx: ServiceContext) {
        println("Initializing RequestForQuote Service on node:" + ignite.cluster().localNode())

        /**
         * It's assumed that the cache has already been deployed. To do that, make sure to start Data Nodes with
         * a respective cache configuration.
         */
        quoteRequestCache = ignite.cache("QuoteRequest")
    }

    /** {@inheritDoc} */
    override fun execute(ctx: ServiceContext) {
        println("Executing QuoteRequest Service on node:" + ignite.cluster().localNode())

        // Some custom logic.
        quoteRequestRepository = applicationContext.getBean(QuoteRequestRepository::class.java)
    }

    /** {@inheritDoc} */
    override fun cancel(ctx: ServiceContext) {
        println("Stopping QuoteRequest Service on node:" + ignite.cluster().localNode())

        // Some custom logic.
    }

    /** {@inheritDoc} */
    override fun addQuoteRequest(quoteRequestId: Int, quoteRequest: QuoteRequest) {
        // quoteRequestCache.put(quoteRequestId, quoteRequest)

        println("incoming Quote Request: id = $quoteRequestId, $quoteRequest")
        quoteRequestRepository.save(quoteRequestId, quoteRequest)
    }

    /** {@inheritDoc} */
    override fun getQuoteRequest(quoteRequestId: Int): QuoteRequest {
        return quoteRequestCache.get(quoteRequestId)
    }

    /** {@inheritDoc} */
    override fun removeQuoteRequest(quoteRequestId: Int) {
        quoteRequestCache.remove(quoteRequestId)
    }
}
