plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "puredash"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":plugin-api"))
    
    testImplementation(libs.bundles.testing)
}

tasks.test {
    useJUnitPlatform()
}
