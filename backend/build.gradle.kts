plugins {
    kotlin("jvm")
}

group = "com.hyosakura"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}