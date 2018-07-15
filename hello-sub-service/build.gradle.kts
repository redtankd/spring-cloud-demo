dependencies {
    implementation(kotlin("reflect"))

    "org.springframework.boot:spring-boot".let {
        implementation("$it-starter-webflux")
    }
}