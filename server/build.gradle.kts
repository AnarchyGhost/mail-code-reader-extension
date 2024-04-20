val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
}

group = "com.anarchyghost"
version = "0.0.1"

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        imageTag.set("0.0.1")
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "mail-code-reader-server" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD"),
            )
        )
    }
}

application {
    mainClass.set("com.anarchyghost.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.3")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.6.3")

    implementation("io.ktor:ktor-server-cors:$ktor_version")

    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.sun.mail:jakarta.mail:2.0.1")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
