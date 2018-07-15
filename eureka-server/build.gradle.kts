dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-eureka-server")
    }

    implementation("javax.xml.bind:jaxb-api:2.3.0")
}