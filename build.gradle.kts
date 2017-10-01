import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.1.50"
    // kotlin("jvm", kotlinVersion)
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    // kotlin("plugin.spring", kotlinVersion)
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false

    id("io.spring.dependency-management") version "1.0.3.RELEASE"
    id("org.springframework.boot") version "2.0.0.M4" apply false // translated in settings.gradle
    id("org.junit.platform.gradle.plugin") version "1.0.0" apply false // translated in settings.gradle
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")

        plugin("io.spring.dependency-management")
        plugin("org.junit.platform.gradle.plugin")
    }

    group = "org.redtank.demo"
    version = "0.0.1-SNAPSHOT"

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    dependencyManagement {
        dependencies {
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:Finchley.BUILD-SNAPSHOT")
                mavenBom("org.apache.curator:apache-curator:4.0.0")
            }
            dependency("org.apache.zookeeper:zookeeper:3.4.9")
            dependencySet("com.fasterxml.jackson.core:2.9.1") {
                entry("jackson-databind")
                entry("jackson-annotations")
                entry("jackson-core")
            }
            dependencySet("org.apache.ignite:2.2.0") {
                entry("ignite-core")
                entry("ignite-spring")
                entry("ignite-spring-data")
                entry("ignite-indexing")
            }
            dependencySet("io.vertx:3.5.0.Beta1") {
                entry("vertx-core")
                entry("vertx-ignite")
            }
            dependencySet("org.junit.jupiter:5.0.0") {
                entry("junit-jupiter-engine")
            }
        }
    }

    repositories {
        jcenter()
        mavenCentral()
        maven { setUrl("https://repo.spring.io/milestone") }
        maven { setUrl("https://repo.spring.io/snapshot") }
    }
}

//dependencies {
//    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8")
//    compile("org.jetbrains.kotlin:kotlin-reflect")
//
//    compile("org.springframework.boot:spring-boot-starter")
//    compile("org.springframework.data:spring-data-commons:1.13.1.RELEASE")
//
//    compile("org.apache.ignite:ignite-core:$igniteVersion")
//    compile("org.apache.ignite:ignite-spring:$igniteVersion")
//    compile("org.apache.ignite:ignite-spring-data:$igniteVersion")
//    compile("org.apache.ignite:ignite-indexing:$igniteVersion")
//
//    testCompile("org.springframework.boot:spring-boot-starter-test") {
//        exclude(module = "junit")
//    }
//    testCompile("org.junit.jupiter:junit-jupiter-api")
//    testRuntime("org.junit.jupiter:junit-jupiter-engine")
//}

