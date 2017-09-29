
plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "cluster.DataNodeStartupKt"
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.apache.ignite:ignite-core")
    implementation("org.apache.ignite:ignite-indexing")
    implementation("org.apache.ignite:ignite-spring")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}