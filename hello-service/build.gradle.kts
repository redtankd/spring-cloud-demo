
plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.HelloServiceKt"
}

dependencies {
    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-webflux")
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-netflix-hystrix")
    }
}