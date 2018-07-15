val springbootVersion: String by settings
val kotlinVersion: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if ("org.springframework.boot" == requested.id.toString()) {
                useVersion(springbootVersion)
            }
            if (requested.id.toString().startsWith("org.jetbrains.kotlin")) {
                useVersion(kotlinVersion)
            }
        }
    }

    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
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