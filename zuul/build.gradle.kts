dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-netflix-zuul")
    }
}