plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.allopen") version "1.9.23"
    id("io.gatling.gradle") version "3.10.5.1"
}

gatling {
    logLevel = "WARN" // logback root level
    logHttp = io.gatling.gradle.LogHttp.NONE // set to 'ALL' for all HTTP traffic in TRACE, 'FAILURES' for failed HTTP traffic in DEBUG

    enterprise.closureOf<Any> {
    }
}

repositories {
    mavenCentral()
}
