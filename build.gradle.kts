import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// see settings.gradle
plugins {
    java // dependencies {} require
    id("io.spring.dependency-management") version "1.0.4.RELEASE"

    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
}

subprojects{
    apply {
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
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
        implementation(kotlin("stdlib-jdk8"))

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
                val springbootVersion: String by project
                val kotlinVersion: String by project
                mavenBom("org.springframework.boot:spring-boot-dependencies:${springbootVersion}") {
                    bomProperty("kotlin.version", "$kotlinVersion")
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