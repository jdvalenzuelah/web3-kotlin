import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    application
    kotlin("jvm") version "1.4.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    jcenter()
}

group = "com.github.web3client"
version = "1.0.0"

application {
    mainClassName = "com.github.web3client.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.web3j:core:4.8.4")

    implementation("com.github.kwebio:kweb-core:0.8.6")
    implementation("com.github.ajalt.clikt:clikt:3.1.0")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnitPlatform {
        includeEngines("junit-jupiter","spek2")
    }

    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "failed", "skipped")
    }
}

tasks.withType<KotlinCompile> {

    sourceCompatibility = "11"
    targetCompatibility = "11"

    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.4"
        languageVersion = "1.4"
    }
}

tasks.wrapper {
    gradleVersion = "6.6.1"
}
