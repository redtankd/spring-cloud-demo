dependencies {
    implementation(kotlin("reflect"))

    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-webflux")
    }

    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-starter-openfeign")
    }
}