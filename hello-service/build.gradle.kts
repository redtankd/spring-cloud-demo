plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.HelloServiceKt"
}

dependencies {
    implementation(kotlin("reflect"))

    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-webflux")
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-openfeign")

        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-config")
        implementation("$it-starter-bus-amqp")
        implementation("$it-starter-netflix-hystrix")
        implementation("$it-starter-zipkin")
    }
}