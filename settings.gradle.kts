pluginManagement {
    resolutionStrategy {
        eachPlugin {
            // id("org.springframework.boot") doesn't work for milestone versions
            /*
            if ("org.springframework.boot" == requested.id.toString()) {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
            }
            */
        }
    }

    repositories {
        gradlePluginPortal()
        maven(url = "https://repo.maven.apache.org/maven2/")
        maven(url = "https://repo.spring.io/plugins-snapshot/")
    }
}

rootProject.name = "spring-cloud-demo"

// service discovery
include("eureka-server")

// distributed configuration
include("config-server")

// gateway and proxy and filter
include("gateway")
include("zuul")

// service
include("hello-service")
include("hello-sub-service")

// metric
include("hystrix-dashboard")
include("zipkin-server")