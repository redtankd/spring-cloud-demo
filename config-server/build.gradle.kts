plugins {
    id("application")
}

apply {
    plugin("org.springframework.boot")
}

application {
    mainClassName = "config.StandaloneGitServerApplicationKt"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.cloud:spring-cloud-config-server")
}