package cluster

import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder

fun discoverySpi() = TcpDiscoverySpi().apply {
    ipFinder = TcpDiscoveryVmIpFinder()
            .setAddresses((47500..47509).map { "127.0.0.1:${it}" })
}