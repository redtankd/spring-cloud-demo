plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.ZuulKt"
}

dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-zuul")
        implementation("$it-starter-netflix-eureka-client")
    }
}