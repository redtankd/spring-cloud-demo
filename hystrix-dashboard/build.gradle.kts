dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-hystrix-dashboard")
        implementation("$it-starter-netflix-turbine")

        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-config")
        implementation("$it-starter-bus-amqp")
    }
}