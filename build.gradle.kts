import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import com.palantir.gradle.docker.DockerExtension

// the version for kotlin and spring-boot is in settings.gradle and gradle.properties.
plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("io.spring.dependency-management") version "1.0.5.RELEASE"
    id("org.springframework.boot") apply false

    id("com.palantir.docker") version "0.20.1" apply false
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")           // kotlin("jvm")
        plugin("org.jetbrains.kotlin.plugin.spring") // kotlin("plugin.spring")

        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")

        plugin("com.palantir.docker")
    }

    group = "org.redtank.springcloud"
    version = "0.0.1-SNAPSHOT"

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

        name = "${project.group}/${bootJar.baseName}:${bootJar.version}"

        files("${bootJar.archivePath}")

        buildArgs(mapOf(
                "JAR_FILE" to bootJar.archiveName
        ))
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))

        "org.springframework.boot:spring-boot".let {
            implementation("$it-starter-actuator")
            implementation("$it-starter-aop") // for retry connecting config server, eureka-server and config-server don't need
        }

        implementation("org.springframework.retry:spring-retry") // for retry connecting config server, eureka-server and config-server don't need

        if (!listOf("eureka-server", "config-server", "hystrix-dashboard").contains(project.name)) {
            "org.springframework.cloud:spring-cloud".let {
                implementation("$it-starter-netflix-eureka-client")
                implementation("$it-starter-config")
                implementation("$it-starter-bus-amqp")
                implementation("$it-starter-netflix-hystrix")
                implementation("$it-starter-zipkin")
            }
        }

        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
    }

    dependencyManagement {
        dependencies {
            imports {
                // Dependencies imported by spring-boot automatically:
                //   com.fasterxml.jackson
                //   org.junit.jupiter
                //   ch.qos.logback
                val springbootVersion: String by project // in gradle.properties
                val kotlinVersion: String by project    // in gradle.properties
                mavenBom("org.springframework.boot:spring-boot-dependencies:$springbootVersion") {
                    bomProperty("kotlin.version", kotlinVersion)
                }

                // Dependencies imported by spring-cloud automatically:
                //   org.apache.curator
                //   org.apache.zookeeper
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:Finchley.RELEASE")
            }

            dependency("org.javamoney:moneta:1.1")
            dependency("org.zalando:jackson-datatype-money:1.0.0")

            dependencySet("org.apache.ignite:2.5.0") {
                entry("ignite-core")
                entry("ignite-spring")
                entry("ignite-spring-data")
                entry("ignite-indexing")
            }
        }
    }

    repositories {
        jcenter()
        mavenCentral()
        maven(url = "https://repo.spring.io/release")
        maven(url = "https://repo.spring.io/milestone")
        maven(url = "https://repo.spring.io/snapshot")
    }
}