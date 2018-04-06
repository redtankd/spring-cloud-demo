plugins {
    application
    id("org.springframework.boot")
}

application {
    mainClassName = "app.EurekaServerKt"
}

dependencies {
    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-actuator")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-eureka-server")
    }

    implementation("javax.xml.bind:jaxb-api:2.3.0")
}