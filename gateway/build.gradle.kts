plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.GatewayKt"
}

dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-gateway")
        implementation("$it-starter-netflix-eureka-client")
    }
}