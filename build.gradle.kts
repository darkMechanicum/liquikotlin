plugins {
    kotlin("jvm") version "1.3.21"
}

group = "com.tsarev"
version = "1.0.0."

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

    testCompile("junit", "junit", "4.12")
    testCompile("com.h2database", "h2", "1.4.199")
}