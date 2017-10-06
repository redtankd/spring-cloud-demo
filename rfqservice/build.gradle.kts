import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "rfq.RequestForQuoteServiceNodeStartupKt"
}

tasks {
    withType<JavaCompile> {
        with(options) {
            // for Ignite's IgniteRepository Interface
            compilerArgs = listOf("-Xlint:unchecked")
        }
    }
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
    implementation("org.apache.ignite:ignite-spring-data")
    implementation("org.springframework.data:spring-data-commons") // spring-data-commons:2.0.0.RC? is not compatible with Ignite
//    implementation("org.springframework.data:spring-data-commons:1.13.7.RELEASE")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}