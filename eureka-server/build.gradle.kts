plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.EurekaServerKt"
}

dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-eureka-server")
    }
}