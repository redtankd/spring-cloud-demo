package cluster

import cluster.filters.DataNodeFilter
import entity.Login
import entity.QuoteRequest
import org.apache.ignite.IgniteException
import org.apache.ignite.IgniteSpringBean
import org.apache.ignite.cache.QueryEntity
import org.apache.ignite.cache.QueryIndex
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import javax.cache.configuration.FactoryBuilder

/**
 * A new Data Node will be started in a separate JVM process.
 *
 * @param args Command line arguments, none required.
 * @throws IgniteException If failed.
 */
fun main(args: Array<String>) {
    SpringApplication(DataNodeApplication::class.java).apply {
        addInitializers(
                ApplicationContextInitializer<GenericApplicationContext> {
                    DataNodeApplication.beans().initialize(it)
                }
        )
    }.run(*args)
}

@SpringBootConfiguration
@EnableAutoConfiguration
class DataNodeApplication {

    companion object {
        fun beans() = beans {
            bean("igniteInstance") {
                // Ignite configuration with all defaults
                // and enabled p2p deployment and enabled events.
                val igniteConfig = IgniteConfiguration().apply {
                    isPeerClassLoadingEnabled = true

                    /*
                        Labeling Data Nodes with special attribute.
                        This attribute is checked by common.filters.DataNodeFilters
                        which decides where caches have to be deployed.
                     */
                    userAttributes = mutableMapOf("data.node" to true)

                    // Configuring caches that will be deployed on Data Nodes
                    setCacheConfiguration(
                            // Cache for QuoteRequest
                            CacheConfiguration<Int, QuoteRequest>().apply {
                                name = "QuoteRequest"
                                /*
                                    Enabling a special nodes filter for the cache. The filter
                                    will make sure that the cache will be deployed only on Data
                                    Nodes, the nodes that have 'data.node' attribute in the local
                                    node map.
                                 */
                                nodeFilter = DataNodeFilter()
                            },
                            // Cache for Login records
                            CacheConfiguration<Int, Login>().apply {
                                name = "maintenance"
                                /*
                                    Enabling a special nodes filter for the cache. The filter
                                    will make sure that the cache will be deployed only on Data
                                    Nodes, the nodes that have 'data.node' attribute in the local
                                    node map.
                                 */
                                nodeFilter = DataNodeFilter()

                                // Enabling our sample cache store for the Login cache
                                setCacheStoreFactory(FactoryBuilder.factoryOf("common.cachestore.SimpleCacheStore"))

                                // Avoid Login objects deserialization on data nodes side
                                // when they are passed to SampleCacheStore.
                                isStoreKeepBinary = true

                                // Enabling the write-through feature for the store.
                                isWriteThrough = true

                                // Enabling the read-through feature for the store.
                                isReadThrough = true

                                // Configuring SQL schema.
                                queryEntities = listOf(
                                        QueryEntity().apply {
                                            // Setting indexed type's key class
                                            keyType = "java.lang.Integer"

                                            // Setting indexed type's value class
                                            valueType = "entity.Login"

                                            // Defining fields that will be either indexed or queryable.
                                            // Indexed fields are added to 'indexes' list below.
                                            fields = linkedMapOf("userId" to "java.lang.Integer")

                                            // Defining indexed fields.
                                            // Single field (aka. column) index
                                            indexes = listOf(QueryIndex("userId"))
                                        }
                                )
                            }
                    )

                    discoverySpi = discoverySpi()
                }

                IgniteSpringBean().apply {
                    configuration = igniteConfig
                }
            }
        }
    }
}

