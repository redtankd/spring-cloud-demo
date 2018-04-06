plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.ConfigServerKt"
}

dependencies {
    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-config-server")
        implementation("$it-config-monitor")

        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-bus-amqp")
    }
}