
plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "cluster.ExternalTestAppKt"
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.apache.ignite:ignite-core")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}