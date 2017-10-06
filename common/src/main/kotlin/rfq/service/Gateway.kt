package rfq.service

import entity.Command
import org.apache.ignite.services.Service

/**
 * Service interface which defines rfq.service specific methods that are visible to every cluster node that is
 * going to interact with the rfq.service using {@link IgniteServices} API. In general, the interface is not only used
 * by rfq.service implementations but also needed for the nodes that will talk to the rfq.service by means of rfq.service proxy -
 * {@link IgniteServices#serviceProxy(String, Class, boolean)}.
 */
interface Gateway : Service {

    fun executeCommand(command: Command)

}
