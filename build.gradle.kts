import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import com.palantir.gradle.docker.DockerExtension

plugins {
    java // `implementation` and `testImplementation` require

    val kotlinVersion = "1.3.10"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false

    id("org.springframework.boot") version "2.1.0.RELEASE" apply false

    id("com.palantir.docker") version "0.20.1" apply false
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")           // kotlin("jvm")
        plugin("org.jetbrains.kotlin.plugin.spring") // kotlin("plugin.spring")

        plugin("org.springframework.boot")

        plugin("com.palantir.docker")
    }

    group = "org.redtank.springcloud"
    version = "0.0.1-SNAPSHOT"

    // --------------------------------------------------
    // Tasks Configuration

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }

    configure<DockerExtension> {
        val bootJar: BootJar by tasks

        dependsOn(bootJar)

        name = "redtankd/${bootJar.baseName}:${bootJar.version}"

        // com.palantir.gradle.docker copys files to a temp dir.
        files("${bootJar.archivePath}")

        buildArgs(mapOf(
                "JAR_FILE" to bootJar.archiveName
        ))
    }

    // --------------------------------------------------
    // Dependencies Configuration

    dependencies {
        // --------------------------------------------------
        // Dependencies Management

        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.1.0.RELEASE"))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:Greenwich.M3"))

        constraints {
            val kotlinVersion = "1.3.10"
            implementation(kotlin("stdlib-jdk8", kotlinVersion))
            implementation(kotlin("reflect", kotlinVersion))

            implementation("org.javamoney:moneta:1.1")
            implementation("org.zalando:jackson-datatype-money:1.0.0")

            "org.apache.ignite:ignite".let {
                val version = "2.6.0"
                implementation("$it-core:$version")
                implementation("$it-spring:$version")
                implementation("$it-spring-data:$version")
                implementation("$it-indexing:$version")
            }
        }

        // --------------------------------------------------
        // Common Dependencies

        implementation(kotlin("stdlib-jdk8"))

        "org.springframework.boot:spring-boot".let {
            implementation("$it-starter-actuator")
        }

        if (!listOf("eureka-server", "config-server", "hystrix-dashboard").contains(project.name)) {
            "org.springframework.cloud:spring-cloud".let {
                implementation("$it-starter-netflix-eureka-client")
                implementation("$it-starter-config")
                implementation("$it-starter-bus-amqp")
                implementation("$it-starter-netflix-hystrix")
                implementation("$it-starter-zipkin")
            }
        }

        // for retrying to connect to config server
        if (!listOf("eureka-server", "config-server").contains(project.name)) {
            implementation("org.springframework.boot:spring-boot-starter-aop")
            implementation("org.springframework.retry:spring-retry")
        }

        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
    }

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://repo.spring.io/release")
        maven(url = "https://repo.spring.io/milestone")
        maven(url = "https://repo.spring.io/snapshot")
    }
}