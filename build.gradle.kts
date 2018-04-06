import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//TODO: maybe variables is support in plugins {} someday
plugins {
    java // dependencies {} require
    id("io.spring.dependency-management") version "1.0.4.RELEASE"

    kotlin("jvm")           version "1.2.31" apply false
    kotlin("plugin.spring") version "1.2.31" apply false

    // uncomment settings.gradle.kts if milestone versions is used
    id("org.springframework.boot") version "2.0.1.RELEASE" apply false
}

subprojects{
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.spring.dependency-management")
    }

    group = "org.redtank.demo"
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

    dependencies {
        implementation(kotlin("stdlib-jre8"))

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
                //   org.jetbrains.kotlin, which's version is overridden
                // TODO: the same version number as in plugins {}
                mavenBom("org.springframework.boot:spring-boot-dependencies:2.0.1.RELEASE") {
                    bomProperty("kotlin.version", "1.2.31")
                }

                // Dependencies imported by spring-cloud automatically:
                //   org.apache.curator
                //   org.apache.zookeeper
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:Finchley.M9")
            }

            dependency("org.javamoney:moneta:1.1")
            dependency("org.zalando:jackson-datatype-money:1.0.0")

            dependencySet("org.apache.ignite:2.4.0") {
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
        maven(url = "https://repo.spring.io/milestone")
        maven(url = "https://repo.spring.io/snapshot")
    }
}