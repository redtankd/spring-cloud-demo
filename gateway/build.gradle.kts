
plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
//    mainClassName = "cluster.GatewayNodeStartupKt"
    mainClassName = "Application"
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.apache.ignite:ignite-core")
    implementation("org.apache.ignite:ignite-spring")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")

//    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-all")
//    implementation("org.springframework.cloud:spring-cloud-starter-feign")
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
}