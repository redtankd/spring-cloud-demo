plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.HystrixDashboardKt"
}

dependencies {
    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-netflix-turbine")
        implementation("$it-starter-netflix-hystrix-dashboard")
    }
}