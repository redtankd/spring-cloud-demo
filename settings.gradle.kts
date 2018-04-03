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

rootProject.name = "cloud-demo"

// service discovery
include("eureka-server")

// gateway and proxy
include("gateway")
include("zuul")

// metric
include("hystrix-dashboard")

// service
include("hello-service")