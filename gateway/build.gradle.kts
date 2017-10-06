
plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "rfq.GatewayNodeStartupKt"
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // starter-webflux is conflict with starter-web
    // Another solution is excluding "spring-boot-starter-tomcat"
    // implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.springframework.cloud:spring-cloud-zookeeper-discovery")
    implementation("org.apache.curator:curator-framework")
    implementation("org.apache.curator:curator-x-discovery")
    implementation("org.apache.zookeeper:zookeeper")

    implementation("org.apache.ignite:ignite-core")
    implementation("org.apache.ignite:ignite-spring")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}