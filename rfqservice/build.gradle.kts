import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "cluster.RequestForQuoteServiceNodeStartupKt"
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

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.apache.ignite:ignite-core")
    implementation("org.apache.ignite:ignite-indexing")
    implementation("org.apache.ignite:ignite-spring")
    implementation("org.apache.ignite:ignite-spring-data")
    // spring-data-commons:2.0.0.RC? is not compatible with Ignite
    implementation("org.springframework.data:spring-data-commons:1.13.1.RELEASE")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}