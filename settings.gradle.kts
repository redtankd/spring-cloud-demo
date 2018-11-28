pluginManagement {
    resolutionStrategy {
        eachPlugin {
            // the spring-boot's milestone version requires
            if ("org.springframework.boot" == requested.id.toString()
                    && !requested.version.toString().endsWith(".RELEASE")
            ) {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        maven(url = "https://repo.spring.io/milestone/")
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