import com.palantir.gradle.docker.DockerExtension

configure<DockerExtension> {
    files("${project.projectDir}/config-repo")
}

dependencies {
    "org.springframework.cloud:spring-cloud".let {
        implementation("$it-config-server")
        implementation("$it-config-monitor")

        implementation("$it-starter-netflix-eureka-client")
        implementation("$it-starter-bus-amqp")
    }
}