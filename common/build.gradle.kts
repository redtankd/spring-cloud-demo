
plugins {
    id("java-library")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    api("org.apache.ignite:ignite-core")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}