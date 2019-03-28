plugins {
    kotlin("jvm") version "1.3.21"
    `maven-publish`
}

group = "org.bundled"
version = "1.2.2"

configurations {
    "archives"()
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://dl.bintray.com/s1m0nw1/KtsRunner")
}

dependencies {
    compile(kotlin("compiler"))
    compile(kotlin("script-util"))
    compile("org.liquibase:liquibase-core:3.4.2")
    compile("de.swirtz:ktsRunner:0.0.7")
}