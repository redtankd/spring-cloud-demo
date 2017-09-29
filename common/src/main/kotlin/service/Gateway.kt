package service

import entity.Command
import org.apache.ignite.services.Service

/**
 * Service interface which defines service specific methods that are visible to every cluster node that is
 * going to interact with the service using {@link IgniteServices} API. In general, the interface is not only used
 * by service implementations but also needed for the nodes that will talk to the service by means of service proxy -
 * {@link IgniteServices#serviceProxy(String, Class, boolean)}.
 */
interface Gateway : Service {

    fun executeCommand(command: Command)

}
