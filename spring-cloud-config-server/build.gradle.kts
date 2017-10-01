plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "app.StandaloneGitServerApplicationKt"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.cloud:spring-cloud-config-server")

//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//
//    implementation("org.springframework.cloud:spring-cloud-starter")
//    implementation("org.springframework.cloud:spring-cloud-starter-feign")
//    implementation("org.springframework.cloud:spring-cloud-zookeeper-discovery")
//    implementation("org.apache.curator:curator-framework")
//    implementation("org.apache.curator:curator-x-discovery")
//    implementation("org.apache.zookeeper:zookeeper")
}