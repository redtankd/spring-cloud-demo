plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.GatewayKt"
}

dependencies {
    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-gateway")

        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-config")
        implementation("$it-starter-bus-amqp")
        implementation("$it-starter-netflix-hystrix")
        implementation("$it-starter-zipkin")
    }
}