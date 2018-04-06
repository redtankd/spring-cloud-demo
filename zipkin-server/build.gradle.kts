plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.ZipkinServerKt"
}

dependencies {
    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-config")
        implementation("$it-starter-bus-amqp")
    }

    implementation("io.zipkin.java:zipkin-server:2.7.0")
    implementation("io.zipkin.java:zipkin-autoconfigure-ui:2.7.0")
}

